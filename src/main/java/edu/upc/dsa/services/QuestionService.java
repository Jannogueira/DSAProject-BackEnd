package edu.upc.dsa.services;

import edu.upc.dsa.WebManagerImpl;
import edu.upc.dsa.models.FAQ;
import edu.upc.dsa.models.Question;
import edu.upc.dsa.util.JwtUtil;
import io.swagger.annotations.Api;
import edu.upc.dsa.WebManager;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.List;

@Api(value = "/QuestionsAndFAQs", description = "Endpoint to Question Service")
@Path("")
public class QuestionService {
    WebManager wm = WebManagerImpl.getInstance();
    public QuestionService() {
    }

    @Path("/question")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response receiveQuestion(
            @HeaderParam("Authorization") String tokenHeader,
            @FormParam("titulo") String titulo,
            @FormParam("mensaje") String mensaje) {

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"status\":false, \"message\":\"Token no válido\"}").build();
        }

        String token = tokenHeader.substring("Bearer ".length());
        String user = JwtUtil.getUsernameFromToken(token);

        System.out.println("Pregunta recibida de: " + user);

        wm.crearPregunta(user, titulo, mensaje);

        return Response.status(Response.Status.CREATED)
                .entity("{\"status\":true, \"message\":\"Pregunta recibida\"}")
                .build();
    }
    @GET
    @Path("")
    @ApiOperation(value = "Get All FAQs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<FAQ> getAllFAQs() {
        List<FAQ> faqs = wm.getAllFAQs();
        return faqs;
    }

}
