package rest;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.*;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import facades.MovieFacade;
import utils.EMF_Creator;

@Path("movies")
public class MovieFetchResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final MovieFacade FACADE = MovieFacade.getInstance(EMF);
    Gson GSON = new Gson();
    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies() throws IOException, ExecutionException, InterruptedException {
        return Response.ok().entity(GSON.toJson(FACADE.getAllMovies())).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@PathParam("id") String id) throws IOException, ExecutionException, InterruptedException {
        return Response.ok().entity(GSON.toJson(FACADE.getMovie(id))).build();
    }
}