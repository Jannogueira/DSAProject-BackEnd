package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;

import edu.upc.dsa.models.Items;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

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
        return wm.getAllShopItems();
    }


}