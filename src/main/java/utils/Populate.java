package utils;


import com.google.common.base.Strings;
import facades.MovieFacade;
import facades.UserFacade;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Populate {
    private final EntityManagerFactory emf;


    public static void main(String[] args) {
        new Populate(EMF_Creator.createEntityManagerFactory()).populateAll();
    }

    public Populate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public List<String> populateAll() {
        List<String> populated = new ArrayList<>();
        if(populateUsers())
            populated.add("users");
        if(populateMovies())
            populated.add("movies");

        return populated;
    }

    /**
     *
     * @return Boolean regarding table being populated or not.
     *
     * */
    public boolean populateUsers() throws IllegalArgumentException {
        UserFacade userFacade = UserFacade.getUserFacade(this.emf);

        if (!userFacade.getUsers().isEmpty()) return false;

        // NOTICE: Always set your password as environment variables.
        String password_admin = "test";
        String password_user = "test";

        boolean isDeployed = System.getenv("DEPLOYED") != null;
        if(isDeployed) {
            password_user = System.getenv("PASSWORD_DEFAULT_USER");
            password_admin = System.getenv("PASSWORD_DEFAULT_ADMIN");

            // Do not allow "empty" passwords in production.
            if(Strings.isNullOrEmpty(password_admin) || password_admin.trim().length() < 3 || Strings.isNullOrEmpty(password_user) || password_user.trim().length() < 3)
                throw new IllegalArgumentException("FAILED POPULATE OF USERS: Passwords were empty or less than 3 characters? Are environment variables: [PASSWORD_DEFAULT_USER, PASSWORD_DEFAULT_ADMIN] set?");
        }

        userFacade.create("user", password_user, new ArrayList<>());
        userFacade.create("admin", password_admin, Collections.singletonList("admin"));

        return true;
    }

    public boolean populateMovies() throws IllegalArgumentException {
        MovieFacade movieFacade = MovieFacade.getInstance(this.emf);

        if (!movieFacade.getAllMovies().isEmpty()) return false;

        movieFacade.createMovie("tt1663202"); // The Revenant.
        movieFacade.createMovie("tt0993846"); // The Wolf of Wallstreet.

        return true;
    }

}
