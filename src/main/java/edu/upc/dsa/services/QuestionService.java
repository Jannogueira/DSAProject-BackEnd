package edu.upc.dsa.services;

import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.models.FAQ;
import edu.upc.dsa.util.JwtUtil;
import io.swagger.annotations.Api;
import edu.upc.dsa.WebManager;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Api(value = "/QuestionsAndFAQs", description = "Endpoint to Question Service")
@Path("")
public class QuestionService {
    private static final Logger logger = Logger.getLogger(QuestionService.class);
    WebManager wm = WebManagerImpl.getInstance();

    public QuestionService() {}

    @Path("/question")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response receiveQuestion(
            @HeaderParam("Sender") String sender,
            @FormParam("titulo") String titulo,
            @FormParam("mensaje") String mensaje) {

        logger.info("Pregunta recibida de: " + sender + " | Título: " + titulo);

        try {
            wm.crearPregunta(sender, titulo, mensaje);
            logger.info("Pregunta creada correctamente para usuario: " + sender);
            return Response.status(Response.Status.CREATED)
                    .entity("{\"status\":true, \"message\":\"Pregunta recibida\"}")
                    .build();
        } catch (Exception e) {
            logger.error("Error al crear la pregunta para usuario: " + sender, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"status\":false, \"message\":\"Error al crear la pregunta\"}")
                    .build();
        }
    }

    @GET
    @Path("/faqs")
    @ApiOperation(value = "Get All FAQs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FAQ> getAllFAQs() {
        logger.info("Petición para obtener todas las FAQs");
        List<FAQ> faqs = wm.getAllFAQs();
        logger.info("FAQs obtenidas: " + faqs.size());
        return faqs;
    }
}
