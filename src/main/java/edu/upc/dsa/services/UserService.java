package edu.upc.dsa.services;

import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import edu.upc.dsa.WebManagerImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import edu.upc.dsa.models.User;
import java.util.List;

@Api(value = "/users", description = "Endpoint for user registration and login")
@Path("/users")
public class UserService {
    WebManagerImpl wm = WebManagerImpl.getInstance();
    public UserService() {
        this.wm.RegisterUser("Omar089", "omar@gmail.com", "1234");
        this.wm.RegisterUser("VicPin", "victor@gmail.com", "5678");
        this.wm.RegisterUser("Jan", "jan@gmail.com", "123");

    }

    @Path("users")
    @GET
    @ApiOperation(value = "Get All Users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return WebManagerImpl.getInstance().getAllUsers();
    }


    @POST
    @Path("/register")
    @ApiOperation(value = "Register a new user", notes = "Provide username, email, and password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(
            @ApiParam(value = "Username", required = true) @FormParam("username") String username,
            @ApiParam(value = "Email", required = true) @FormParam("correo") String correo,
            @ApiParam(value = "Password", required = true) @FormParam("password") String password) {

        int registro = wm.RegisterUser(username, correo, password);

        switch (registro) {
            case 1: // Registro exitoso
                return Response.ok("{\"status\":true, \"message\":\"Registro Completado\"}")
                        .type(MediaType.APPLICATION_JSON).build();
            case 2: // Usuario ya existe
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"status\":false, \"message\":\"El usuario ya existe\"}")
                        .type(MediaType.APPLICATION_JSON).build();
            case 3: // Correo ya existe
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"status\":false, \"message\":\"El correo ya existe\"}")
                        .type(MediaType.APPLICATION_JSON).build();
            default: // Otro error
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

        boolean success = wm.LoginUser(correo, password);
        if (success) {
            String usuario = wm.usuarioPorCorreo(correo);
            String token = JwtUtil.generateToken(usuario);
            System.out.println("login correcto: " + usuario + " token: " + token);
            return Response.ok("{\"status\":true, \"message\":\"Login exitoso\", \"user\":\"" + usuario + "\", \"token\":\"" + token + "\"}")
                    .type(MediaType.APPLICATION_JSON).build();
        } else {
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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"valid\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());

        boolean valid = JwtUtil.validateToken(token);
        if (valid) {
            System.out.println("Token valido");
            return Response.ok("{\"valid\":true}").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"valid\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }
    }

    @PUT
    @Path("/actualizarUsuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response actualizarUsuario(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("nuevoUsuario") String nuevoUsuario) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer "))
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"status\":false, \"message\":\"Token no válido\"}").build();

        String token = tokenHeader.substring("Bearer ".length());
        String usuario = JwtUtil.getUsernameFromToken(token);

        if (wm.actualizarUsuario(usuario, nuevoUsuario)) {
            return Response.ok("{\"status\":true, \"message\":\"Usuario actualizado\"}").build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"status\":false, \"message\":\"Error al actualizar usuario\"}").build();
    }


    @PUT
    @Path("/actualizarCorreo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response actualizarCorreo(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("nuevoCorreo") String nuevoCorreo) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer "))
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"status\":false, \"message\":\"Token no válido\"}").build();

        String token = tokenHeader.substring("Bearer ".length());
        String usuario = JwtUtil.getUsernameFromToken(token);

        if (wm.actualizarCorreo(usuario, nuevoCorreo)) {
            return Response.ok("{\"status\":true, \"message\":\"Correo actualizado\"}").build();
        }
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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);
        User user = WebManagerImpl.getInstance().getUser(usuario);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }

        if (!user.getPassword().equals(contrasena)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña incorrecta\"}")
                    .build();
        }

        WebManagerImpl.getInstance().eliminarUsuario(usuario);
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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no proporcionado o inválido\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);
        User user = WebManagerImpl.getInstance().getUser(usuario);

        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }

        if (!user.getPassword().equals(contrasenaActual)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña actual incorrecta\"}")
                    .build();
        }

        WebManagerImpl.getInstance().actualizarContrasena(usuario, nuevaContrasena);
        return Response.ok("{\"status\":true, \"message\":\"Contraseña actualizada correctamente\"}")
                .build();
    }



}
