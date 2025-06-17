package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.util.JwtUtil;
import edu.upc.dsa.models.Items;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "/Shop", description = "Endpoint to Shop Service")
@Path("/Shop")
public class ShopService {

    private static final Logger logger = Logger.getLogger(ShopService.class);
    WebManager wm = WebManagerImpl.getInstance();

    public ShopService() {}

    @GET
    @Path("/items")
    @ApiOperation(value = "Get all shop items", notes = "Returns a list of all available shop items")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Items> getShopItems() {
        logger.info("Petición para obtener todos los items de la tienda");
        List<Items> items = wm.getAllItems();
        logger.info("Items obtenidos: " + items.size());
        return items;
    }

    @POST
    @Path("/comprar")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Compra múltiples items", notes = "Envía un mapa de ID de ítem y cantidades, junto con el token en el header.")
    public Response comprarItems(
            @HeaderParam("Authorization") String tokenHeader, String itemsString) {
        itemsString = quitarComillas(itemsString);

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido o no proporcionado en /comprar");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido o no proporcionado\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());

        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en /comprar");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }

        String usuario = JwtUtil.getUsernameFromToken(token);
        Map<Integer, Integer> itemsACobrar = parseItemsString(itemsString);
        logger.info("Usuario " + usuario + " intenta comprar items: " + itemsACobrar);

        int resultado = wm.comprarItems(usuario, itemsACobrar);

        if (resultado > 0) {
            logger.info("Compra exitosa para usuario: " + usuario + ". Dinero restante: " + resultado);
            return Response.ok("{\"status\":true, \"message\":\"Compra exitosa\", \"dineroRestante\":" + resultado + "}").build();
        }
        else {
            switch (resultado) {
                case -1:
                    logger.warn("Usuario no encontrado en /comprar: " + usuario);
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                            .build();
                case -2:
                    logger.warn("Algún item no existe en /comprar para usuario: " + usuario);
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity("{\"status\":false, \"message\":\"Algún item no existe\"}")
                            .build();
                case 0:
                    logger.warn("Dinero insuficiente para usuario: " + usuario);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"status\":false, \"message\":\"Dinero insuficiente\"}")
                            .build();
                default:
                    logger.error("Error inesperado en /comprar para usuario: " + usuario);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"status\":false, \"message\":\"Error inesperado\"}")
                            .build();
            }
        }
    }

    private Map<Integer, Integer> parseItemsString(String itemsString) {
        Map<Integer, Integer> map = new HashMap<>();
        if (itemsString == null || itemsString.trim().isEmpty()) return map;

        String[] pairs = itemsString.replaceAll("\\s+", "").split(",");
        for (String pair : pairs) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                try {
                    int key = Integer.parseInt(kv[0]);
                    int value = Integer.parseInt(kv[1]);
                    map.put(key, value);
                } catch (NumberFormatException e) {
                    logger.warn("Error parseando itemsString, par: " + pair, e);
                }
            }
        }
        return map;
    }
    @POST
    @Path("/consumir")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Consumir un objeto del inventario", notes = "Reduce en 1 la cantidad del objeto especificado")
    public Response consumirItem(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("idObjeto") int idObjeto) {
    System.out.println("RECIBO AUTORIZACION: " + tokenHeader + " Y EL ID OBJETO: " + idObjeto);
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            logger.warn("Token no válido o no proporcionado en /consumir");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido o no proporcionado\"}")
                    .build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        if (!JwtUtil.validateToken(token)) {
            logger.warn("Token inválido o expirado en /consumir");
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token inválido o expirado\"}")
                    .build();
        }
        String username = JwtUtil.getUsernameFromToken(token);
        logger.info("Usuario " + username + " intenta consumir objeto: " + idObjeto);

        try {
            int resultado = wm.consumirObjeto(username, idObjeto);
            switch (resultado) {
                case 1:
                    logger.info("Objeto consumido correctamente para usuario: " + username + ", objeto: " + idObjeto);
                    return Response.ok("{\"status\":true, \"message\":\"Objeto consumido correctamente\"}").build();

                case -1:
                    logger.warn("No quedan unidades disponibles para consumir para usuario: " + username + ", objeto: " + idObjeto);
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"status\":false, \"message\":\"No quedan unidades disponibles para consumir\"}")
                            .build();
                default:
                    logger.error("Error desconocido al consumir objeto para usuario: " + username + ", objeto: " + idObjeto);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                            .entity("{\"status\":false, \"message\":\"Error desconocido\"}")
                            .build();
            }
        }
        catch (IndexOutOfBoundsException e) {
            logger.warn("El objeto no existe en el inventario para usuario: " + username + ", objeto: " + idObjeto, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"El objeto no existe en el inventario\"}")
                    .build();
        }
        catch (NullPointerException e) {
            logger.warn("Usuario no encontrado en /consumir: " + username, e);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"status\":false, \"message\":\"Usuario no encontrado\"}")
                    .build();
        }
        catch (Exception e) {
            logger.error("Error interno al consumir objeto para usuario: " + username + ", objeto: " + idObjeto, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":false, \"message\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    public static String quitarComillas(String texto) {
        if (texto != null && texto.length() >= 2 &&
                texto.startsWith("\"") && texto.endsWith("\"")) {
            return texto.substring(1, texto.length() - 1);
        }
        return texto;
    }


}