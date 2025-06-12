package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;

import edu.upc.dsa.models.Media;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/Media", description = "Endpoint to Shop Service")
@Path("")

public class MediaService {
    WebManager wm = WebManagerImpl.getInstance();

    public MediaService() {}

    @GET
    @Path("/media")
    @ApiOperation(value = "Get all media", notes = "Returns a list of all videos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Media> getMedia() {
        System.out.println("Videos are available!");
        return wm.getAllMedia();
    }

}
