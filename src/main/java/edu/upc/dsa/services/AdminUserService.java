package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.models.Items;
import edu.upc.dsa.util.JwtUtil;
import edu.upc.dsa.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Api(value = "/AdminUser", description = "Endpoint to Shop Service")
@Path("/Admin")
public class AdminUserService {

    private static final Logger logger = Logger.getLogger(AdminUserService.class);
    WebManager wm = WebManagerImpl.getInstance();
    private static final String ADMIN_PASSWORD = "admin123";

    public AdminUserService() {}

    @POST
    @Path("/actualizarUsuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuarioAdmin(
            @FormParam("usuario") String usuario,
            @FormParam("nuevoUsuario") String nuevoUsuario,
            @FormParam("adminPassword") String adminPassword) {

        if (!ADMIN_PASSWORD.equals(adminPassword)) {
            logger.warn("Contraseña de administrador incorrecta en actualizarUsuarioAdmin");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        boolean actualizado = wm.actualizarUsuario(usuario, nuevoUsuario);
        if (actualizado) {
            logger.info("Usuario actualizado por admin: " + usuario + " -> " + nuevoUsuario);
        } else {
            logger.warn("Intento de actualizar usuario inexistente por admin: " + usuario);
        }
        return Response.ok("{\"status\":" + actualizado + ", \"message\":\"Usuario actualizado\"}").build();
    }

    @POST
    @Path("/actualizarCorreo")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarCorreoAdmin(
            @FormParam("usuario") String usuario,
            @FormParam("nuevoCorreo") String nuevoCorreo,
            @FormParam("adminPassword") String adminPassword) {

        if (!ADMIN_PASSWORD.equals(adminPassword)) {
            logger.warn("Contraseña de administrador incorrecta en actualizarCorreoAdmin");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        boolean actualizado = wm.actualizarCorreo(usuario, nuevoCorreo);
        if (actualizado) {
            logger.info("Correo actualizado por admin para usuario: " + usuario + " -> " + nuevoCorreo);
        } else {
            logger.warn("Intento de actualizar correo para usuario inexistente por admin: " + usuario);
        }
        return Response.ok("{\"status\":" + actualizado + ", \"message\":\"Correo actualizado\"}").build();
    }

    @POST
    @Path("/actualizarContrasena")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarContrasenaAdmin(
            @FormParam("usuario") String usuario,
            @FormParam("nuevaContrasena") String nuevaContrasena,
            @FormParam("adminPassword") String adminPassword) {

        if (!ADMIN_PASSWORD.equals(adminPassword)) {
            logger.warn("Contraseña de administrador incorrecta en actualizarContrasenaAdmin");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        String hashedPassword = PasswordUtil.hashPassword(nuevaContrasena);
        boolean actualizado = wm.actualizarContrasena(usuario, hashedPassword);

        if (actualizado) {
            logger.info("Contraseña actualizada por admin para usuario: " + usuario);
        } else {
            logger.warn("Intento de actualizar contraseña para usuario inexistente por admin: " + usuario);
        }
        return Response.ok("{\"status\":" + actualizado + ", \"message\":\"Contraseña actualizada\"}").build();
    }

    @POST
    @Path("/eliminarUsuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminarUsuarioAdmin(
            @FormParam("usuario") String usuario,
            @FormParam("adminPassword") String adminPassword) {

        if (!ADMIN_PASSWORD.equals(adminPassword)) {
            logger.warn("Contraseña de administrador incorrecta en eliminarUsuarioAdmin");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        boolean eliminado = wm.eliminarUsuario(usuario);
        if (eliminado) {
            logger.info("Usuario eliminado por admin: " + usuario);
            return Response.ok("{\"status\":true, \"message\":\"Usuario eliminado correctamente\"}").build();
        } else {
            logger.warn("Intento de eliminar usuario inexistente por admin: " + usuario);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }
    }
}
