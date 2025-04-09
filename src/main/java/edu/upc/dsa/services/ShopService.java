package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;

import edu.upc.dsa.models.ShopItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/Shop", description = "Endpoint to Shop Service")
@Path("/Shop")
public class ShopService {

    WebManager wm = WebManagerImpl.getInstance();

    public ShopService() {

        this.wm.addShopItem(new ShopItem("Bomba"));
        this.wm.addShopItem(new ShopItem("Shaker"));
    }


    @GET
    @Path("/items")
    @ApiOperation(value = "Get all shop items", notes = "Returns a list of all available shop items")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ShopItem> getShopItems() {
        return wm.getAllShopItems();
    }


}