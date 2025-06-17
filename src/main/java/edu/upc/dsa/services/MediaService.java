package edu.upc.dsa.services;

import edu.upc.dsa.WebManager;
import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.models.Media;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/Media", description = "Endpoint to Media Service")
@Path("")
public class MediaService {
    private static final Logger logger = Logger.getLogger(MediaService.class);
    WebManager wm = WebManagerImpl.getInstance();

    public MediaService() {}

    @GET
    @Path("/media")
    @ApiOperation(value = "Get all media", notes = "Returns a list of all videos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Media> getMedia() {
        logger.info("Solicitando lista completa de medios");
        List<Media> mediaList = wm.getAllMedia();
        logger.info("Medios obtenidos: " + mediaList.size() + " elementos");
        return mediaList;
    }
}
