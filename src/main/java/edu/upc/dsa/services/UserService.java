package edu.upc.dsa.services;

import edu.upc.dsa.WebManagerImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import edu.upc.dsa.WebManagerImpl;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import edu.upc.dsa.models.User;
import java.util.List;

@Api(value = "/users", description = "Endpoint for user registration and login")
@Path("/users")
public class UserService {

    WebManagerImpl wm = WebManagerImpl.getInstance();
    public UserService() {
        this.wm.RegisterUser("Omar089", "1234", "omar@gmail.com");
        this.wm.RegisterUser("VicPin", "5678", "victor@gmail.com");
    }

    @Path("users")
    @GET
    @ApiOperation(value = "Get All Users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAllUsers() {
        return WebManagerImpl.getInstance().getAllUsers();
    }


    @POST
    @Path("/register")
    @ApiOperation(value = "Register a new user", notes = "Provide username, email, and password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response register(
            @ApiParam(value = "Username", required = true) @FormParam("username") String username,
            @ApiParam(value = "Email", required = true) @FormParam("correo") String correo,
            @ApiParam(value = "Password", required = true) @FormParam("password") String password) {

        boolean success = wm.RegisterUser(username, correo, password);
        if (success) {
            return Response.status(Response.Status.CREATED).entity("User registered successfully").build();
        } else {
            return Response.status(Response.Status.CONFLICT).entity("User already exists").build();
        }
    }




    @POST
    @Path("/login")
    @ApiOperation(value = "Login a user", notes = "Provide email and password")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response login(
            @ApiParam(value = "Email", required = true) @FormParam("correo") String correo,
            @ApiParam(value = "Password", required = true) @FormParam("password") String password) {

        boolean success = wm.LoginUser(correo, password);
        if (success) {
            return Response.ok().entity("Login successful").build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
    }



}
