package dtos;

import lombok.Data;

import java.util.List;

@Data
public class MovieDTO {
    private String Title;
    private int Year;
    private String Rated;
    private String Released;
    private String Runtime;
    private String Genre;
    private String Director;
    private String Writer;
    private String Actors;
    private String Plot;
    private String Language;
    private String Poster;
    private List<RatingDTO> Ratings;
    private int Metascore;
    private float imdbRating;
    private String Type;

    @Data
    private class RatingDTO {
        private String Source;
        private String Value;
    }
}
