package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.models.Items;
import edu.upc.dsa.util.JwtUtil;
import edu.upc.dsa.util.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import edu.upc.dsa.models.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/AdminUser", description = "Endpoint to Shop Service")
@Path("/Admin")
public class AdminUserService {

    WebManager wm = WebManagerImpl.getInstance();
    private static final String ADMIN_PASSWORD = "admin123";

    public AdminUserService() {

    }
    @POST
    @Path("/actualizarUsuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarUsuarioAdmin(
            @FormParam("usuario") String usuario,
            @FormParam("nuevoUsuario") String nuevoUsuario,
            @FormParam("adminPassword") String adminPassword) {

        if (!ADMIN_PASSWORD.equals(adminPassword)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        boolean actualizado = WebManagerImpl.getInstance().actualizarUsuario(usuario, nuevoUsuario);
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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        boolean actualizado = WebManagerImpl.getInstance().actualizarCorreo(usuario, nuevoCorreo);
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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        // Hashear la nueva contraseña antes de actualizar
        String hashedPassword = PasswordUtil.hashPassword(nuevaContrasena);
        boolean actualizado = WebManagerImpl.getInstance().actualizarContrasena(usuario, hashedPassword);

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
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Contraseña de administrador incorrecta\"}")
                    .build();
        }

        boolean eliminado = WebManagerImpl.getInstance().eliminarUsuario(usuario);
        if (eliminado) {
            return Response.ok("{\"status\":true, \"message\":\"Usuario eliminado correctamente\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }
    }
}