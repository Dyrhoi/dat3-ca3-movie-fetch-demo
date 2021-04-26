package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dtos.*;
import entities.Movie;
import threads.MyCallable;
import utils.EMF_Creator;
import utils.HttpUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory EMF;

    public static MovieFacade getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            EMF = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    //Private Constructor to ensure Singleton
    private MovieFacade() {}

    // Super secure API KEY
    private final String API_KEY = "3654c4b6";
    private final String API_URL = "http://www.omdbapi.com/";

    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public MovieDTO getMovie(String id)  {
        String query = "?i=" + id;
        String url = API_URL + query + "&apikey=" + API_KEY;

        ExecutorService executor = Executors.newCachedThreadPool();
        //long startTime = System.nanoTime();
        Callable<String> getMovieJSONFromAPI = () -> HttpUtils.fetchData(url);;
        Future<String> future = executor.submit(getMovieJSONFromAPI);

        MovieDTO movie;
        try {
            movie = GSON.fromJson(future.get(), MovieDTO.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new WebApplicationException("Error when connecting to external API", 500);
        }

        executor.shutdown();
        return movie;
    }

    public List<MovieDTO> getAllMovies() {
        EntityManager em = EMF.createEntityManager();
        try {
            ExecutorService executor = Executors.newCachedThreadPool();
            List<Future<MovieDTO>> movies = new ArrayList<>();

            TypedQuery<Movie> q = em.createQuery("SELECT m FROM Movie m", Movie.class);
            q.getResultList().forEach(movie -> {
                String query = "?i=" + movie.getId();
                String url = API_URL + query + "&apikey=" + API_KEY;
                Callable<MovieDTO> getMovieJSONFromAPI = () -> GSON.fromJson(HttpUtils.fetchData(url), MovieDTO.class);
                Future<MovieDTO> future = executor.submit(getMovieJSONFromAPI);
                movies.add(future);
            });
            executor.shutdown();

            return movies.stream().map(future -> {
                MovieDTO movie = null;
                try {
                    movie = future.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return movie;
            }).collect(Collectors.toList());
        } finally {
            em.close();
        }

    }

    /**
     *
     * @param id : IMDB ID
     *
     * */
    public MovieDTO createMovie(String id) {
        EntityManager em = EMF.createEntityManager();
        try {
            Movie movie = new Movie(id);
            em.getTransaction().begin();

            em.persist(movie);

            em.getTransaction().commit();
            return getMovie(id);
        } finally {
            em.close();
        }
    }
}