package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.util.JwtUtil;
import edu.upc.dsa.models.Items;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Api(value = "/Shop", description = "Endpoint to Shop Service")
@Path("/Shop")
public class ShopService {

    WebManager wm = WebManagerImpl.getInstance();

    public ShopService() {

    }


    @GET
    @Path("/items")
    @ApiOperation(value = "Get all shop items", notes = "Returns a list of all available shop items")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> getShopItems() {
        System.out.println("Shop Items are available!");
        return wm.getAllItems();
        //return wm.getAllShopItems();
    }
    /*@POST
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Compra múltiples items", notes = "Envía un mapa de ID de ítem y cantidades, junto con el token en el header.")
    public Response comprarItems(
            @HeaderParam("Authorization") String tokenHeader,
            java.util.Map<Integer, Integer> itemsACobrar) {

        // Validación del token
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido o no proporcionado\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());

        if (!JwtUtil.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);

        int resultado = wm.comprarItems(usuario, itemsACobrar);

        switch (resultado) {
            case -1:
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                        .build();
            case -2:
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":false, \"message\":\"Algún item no existe\"}")
                        .build();
            case 0:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":false, \"message\":\"Dinero insuficiente\"}")
                        .build();
            case 1:
                return Response.ok("{\"status\":true, \"message\":\"Compra exitosa\"}")
                        .build();
            default:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"status\":false, \"message\":\"Error inesperado\"}")
                        .build();
        }
    }*/
    /*@POST
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Compra múltiples items", notes = "Envía un mapa de ID de ítem y cantidades.")
    public Response comprarItems(java.util.Map<Integer, Integer> itemsACobrar) {
        // Para este ejemplo, asumimos que el usuario es fijo o extraído de otro modo
        String usuario = "Jan"; // Puedes reemplazar esto con otra lógica si lo deseas

        int resultado = wm.comprarItems(usuario, itemsACobrar);

        switch (resultado) {
            case -1:
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                        .build();
            case -2:
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"status\":false, \"message\":\"Algún item no existe\"}")
                        .build();
            case 0:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"status\":false, \"message\":\"Dinero insuficiente\"}")
                        .build();
            case 1:
                return Response.ok("{\"status\":true, \"message\":\"Compra exitosa\"}")
                        .build();
            default:
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("{\"status\":false, \"message\":\"Error inesperado\"}")
                        .build();
        }
    }*/
    @POST
    @Path("/comprar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response comprar(Map<String, Integer> items) {
        return Response.ok("{\"status\":true, \"message\":\"Recibido " + items.size() + " items\"}").build();
    }



}