package edu.upc.dsa.services;

import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.models.InsigniaDTO;
import edu.upc.dsa.models.UsersScoreDTO;
import edu.upc.dsa.util.JwtUtil;
import edu.upc.dsa.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import edu.upc.dsa.models.Users;
import java.util.List;

@Api(value = "/users", description = "Endpoint for user registration and login")
@Path("")
public class UserService {
    private static final Logger logger = Logger.getLogger(UserService.class);
    WebManagerImpl wm = WebManagerImpl.getInstance();

    public UserService() {}

    @Path("users")
    @GET
    @ApiOperation(value = "Get All Users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> getAllUsers() {
        logger.info("Petición para obtener todos los usuarios");
        return wm.getAllUsers();
    }

    @POST
    @Path("/register")
    @ApiOperation(value = "Register a new user", notes = "Provide username, email, and password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(
            @ApiParam(value = "Username", required = true) @FormParam("username") String username,
            @ApiParam(value = "Email", required = true) @FormParam("correo") String correo,
            @ApiParam(value = "Password", required = true) @FormParam("password") String password) {

        int registro = wm.registerUser(username, correo, password);

        switch (registro) {
            case 1:
                logger.info("Registro completado para usuario: " + username + ", correo: " + correo);
                return Response.ok("{\"status\":true, \"message\":\"Registro Completado\", \"username\":\"" + username + "\", \"correo\":\"" + correo + "\"}")
                        .type(MediaType.APPLICATION_JSON).build();
            case 2:
                logger.warn("Intento de registro con usuario ya existente: " + username);
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"status\":false, \"message\":\"El usuario ya existe\"}")
                        .type(MediaType.APPLICATION_JSON).build();
            case 3:
                logger.warn("Intento de registro con correo ya existente: " + correo);
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"status\":false, \"message\":\"El correo ya existe\"}")
                        .type(MediaType.APPLICATION_JSON).build();
            default:
                logger.error("Error inesperado durante el registro de usuario: " + username);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"status\":false, \"message\":\"Error inesperado\"}")
                        .type(MediaType.APPLICATION_JSON).build();
        }
    }

    @POST
    @Path("/login")
    @ApiOperation(value = "Login a user", notes = "Provide email and password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @ApiParam(value = "Email", required = true) @FormParam("correo") String correo,
            @ApiParam(value = "Password", required = true) @FormParam("password") String password) {
        boolean success = wm.loginUser(correo, password);
        if (success) {
            String usuario = wm.getUsername(correo);
            String token = JwtUtil.generateToken(usuario);
            logger.info("Login correcto: " + usuario + " token: " + token);
            return Response.ok("{\"status\":true, \"message\":\"Login exitoso\", \"user\":\"" + usuario + "\", \"token\":\"" + token + "\", \"correo\":\"" + correo + "\"}")
                    .type(MediaType.APPLICATION_JSON).build();
        } else {
            logger.warn("Intento de login fallido para correo: " + correo);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Credenciales incorrectas\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @GET
    @Path("/validate")
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateToken(@HeaderParam("Authorization") String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no proporcionado o inválido en validación");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"valid\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());

        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en validación");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"valid\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);

        if (!wm.existeUser(usuario)) {
            logger.warn("Usuario no existe para el token proporcionado");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"valid\":false, \"message\":\"Usuario ya no existe\"}")
                    .build();
        }

        logger.info("Token validado correctamente para usuario: " + usuario);
        return Response.ok("{\"valid\":true}").build();
    }

    @PUT
    @Path("/actualizarUsuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuario(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("nuevoUsuario") String nuevoUsuario) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido en actualizarUsuario");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido\"}").build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        String usuario = JwtUtil.getUsernameFromToken(token);

        if (wm.existeUser(nuevoUsuario)) {
            logger.warn("Intento de actualizar a un nombre de usuario ya en uso: " + nuevoUsuario);
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"status\":false, \"message\":\"El nombre de usuario ya está en uso\"}")
                    .build();
        }

        boolean actualizado = wm.actualizarUsuario(usuario, nuevoUsuario);

        if (actualizado) {
            String nuevoToken = JwtUtil.generateToken(nuevoUsuario);
            logger.info("Usuario actualizado de " + usuario + " a " + nuevoUsuario);
            return Response.ok("{\"status\":true, \"message\":\"Usuario actualizado\", \"newToken\":\"" + nuevoToken + "\"}")
                    .build();
        }

        logger.error("Error al actualizar usuario: " + usuario);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("{\"status\":false, \"message\":\"Error al actualizar usuario\"}")
                .build();
    }

    @PUT
    @Path("/actualizarCorreo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response actualizarCorreo(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("nuevoCorreo") String nuevoCorreo) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido en actualizarCorreo");
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"status\":false, \"message\":\"Token no válido\"}").build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        String usuario = JwtUtil.getUsernameFromToken(token);

        if (wm.actualizarCorreo(usuario, nuevoCorreo)) {
            logger.info("Correo actualizado para usuario: " + usuario);
            return Response.ok("{\"status\":true, \"message\":\"Correo actualizado\"}").build();
        }
        logger.error("Error al actualizar correo para usuario: " + usuario);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"status\":false, \"message\":\"Error al actualizar correo\"}").build();
    }

    @POST
    @Path("/eliminarUsuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuario(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("contrasena") String contrasena) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no proporcionado o inválido en eliminarUsuario");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en eliminarUsuario");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);
        Users users = WebManagerImpl.getInstance().getUser(usuario);

        if (users == null) {
            logger.warn("Usuario no encontrado en eliminarUsuario: " + usuario);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }

        if (!PasswordUtil.verifyPassword(contrasena, users.getPassword())) {
            logger.warn("Contraseña incorrecta en eliminarUsuario para usuario: " + usuario);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña incorrecta\"}")
                    .build();
        }

        WebManagerImpl.getInstance().eliminarUsuario(usuario);
        logger.info("Usuario eliminado correctamente: " + usuario);
        return Response.ok("{\"status\":true, \"message\":\"Usuario eliminado correctamente\"}").build();
    }

    @POST
    @Path("/actualizarContrasena")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarContrasena(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("contrasenaActual") String contrasenaActual,
            @FormParam("nuevaContrasena") String nuevaContrasena) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no proporcionado o inválido en actualizarContrasena");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en actualizarContrasena");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);
        Users users = WebManagerImpl.getInstance().getUser(usuario);

        if (users == null) {
            logger.warn("Usuario no encontrado en actualizarContrasena: " + usuario);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }

        if (!PasswordUtil.verifyPassword(contrasenaActual, users.getPassword())) {
            logger.warn("Contraseña actual incorrecta en actualizarContrasena para usuario: " + usuario);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña actual incorrecta\"}")
                    .build();
        }

        WebManagerImpl.getInstance().actualizarContrasena(usuario, nuevaContrasena);
        logger.info("Contraseña actualizada correctamente para usuario: " + usuario);
        return Response.ok("{\"status\":true, \"message\":\"Contraseña actualizada correctamente\"}")
                .build();
    }

    @GET
    @Path("/correoPorToken")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCorreoPorToken(@HeaderParam("Authorization") String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no proporcionado o inválido en getCorreoPorToken");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());

        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en getCorreoPorToken");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String username = JwtUtil.getUsernameFromToken(token);
        Users users = WebManagerImpl.getInstance().getUser(username);

        if (users == null) {
            logger.warn("Usuario no encontrado en getCorreoPorToken: " + username);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }

        logger.info("Correo consultado correctamente para usuario: " + username);
        return Response.ok("{\"status\":true, \"email\":\"" + users.getCorreo() + "\"}")
                .build();
    }

    @GET
    @Path("/userStats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScoreAndMoney(@HeaderParam("Authorization") String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no proporcionado o inválido en getScoreAndMoney");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }
        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en getScoreAndMoney");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String username = JwtUtil.getUsernameFromToken(token);
        Integer score = wm.getScore(username);
        Integer money = wm.getMoney(username);

        if (score == null || money == null) {
            logger.warn("Usuario no encontrado en getScoreAndMoney: " + username);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }

        logger.info("Stats consultadas correctamente para usuario: " + username);
        String jsonResponse = String.format("{\"status\":true, \"username\":\"%s\", \"score\":%d, \"money\":%d}", username, score, money);
        return Response.ok(jsonResponse).build();
    }

    @GET
    @Path("/inventario")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInventarioUsuario(@HeaderParam("Authorization") String username) {
        logger.info("Inventario consultado para usuario: " + username);
        String jsonInventario = wm.getInventarioPorUsuario(username);
        return Response.ok(jsonInventario).build();
    }

    @GET
    @Path("/ranking")
    @Produces(MediaType.APPLICATION_JSON)
    public List<UsersScoreDTO> getAllUsernamesAndScores() {
        logger.info("Ranking consultado");
        return wm.getAllUsersScoresDTO();
    }

    @GET
    @Path("/user/{username}/badges/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<InsigniaDTO> getUserBadges(@PathParam("username") String username) {
        logger.info("Insignias consultadas para usuario: " + username);
        return WebManagerImpl.getInstance().getUserInsignia(username);
    }

    @POST
    @Path("/insignia")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response anadirInsignia(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("id") int idInsignia) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido en anadirInsignia");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());

        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en anadirInsignia");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String username = JwtUtil.getUsernameFromToken(token);

        int resultado = wm.anadirInsignia(username, idInsignia);

        if (resultado == 1) {
            logger.info("Insignia añadida correctamente para usuario: " + username);
            return Response.ok("{\"status\":true, \"message\":\"Insignia añadida correctamente\"}").build();
        } else if (resultado == 0) {
            logger.warn("La insignia ya ha sido añadida anteriormente para usuario: " + username);
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"status\":false, \"message\":\"La insignia ya ha sido añadida anteriormente\"}")
                    .build();
        } else {
            logger.error("Error al añadir la insignia para usuario: " + username);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":false, \"message\":\"Error al añadir la insignia\"}")
                    .build();
        }
    }

    @POST
    @Path("/actualizarPuntuacion")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Actualizar puntuación máxima", notes = "Actualiza la puntuación máxima del usuario si es mayor a la actual")
    public Response actualizarPuntuacion(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("puntuacion") int puntuacion) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido o no proporcionado en actualizarPuntuacion");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido o no proporcionado\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en actualizarPuntuacion");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);

        try {
            wm.nuevaPuntuacion(usuario, puntuacion);
            logger.info("Puntuación actualizada correctamente para usuario: " + usuario);
            return Response.ok("{\"status\":true, \"message\":\"Puntuación actualizada correctamente\"}").build();
        }
        catch (NullPointerException e) {
            logger.error("Usuario no encontrado en actualizarPuntuacion: " + usuario, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }
        catch (Exception e) {
            logger.error("Error en actualizarPuntuacion para usuario: " + usuario, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":false, \"message\":\"Error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/anadirDinero")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Añadir dinero al usuario", notes = "Incrementa el dinero disponible del usuario")
    public Response anadirDinero(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("cantidad") int cantidad) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido o no proporcionado en anadirDinero");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido o no proporcionado\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en anadirDinero");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);

        try {
            int nuevoSaldo = wm.anadirDinero(usuario, cantidad);
            logger.info("Dinero actualizado para usuario: " + usuario + ", nuevo saldo: " + nuevoSaldo);
            return Response.ok("{\"status\":true, \"message\":\"Dinero actualizado\", \"nuevoSaldo\":" + nuevoSaldo + "}").build();
        }
        catch (NullPointerException e) {
            logger.error("Usuario no encontrado en anadirDinero: " + usuario, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }
        catch (Exception e) {
            logger.error("Error en anadirDinero para usuario: " + usuario, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":false, \"message\":\"Error: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}