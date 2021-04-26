package dtos;

import lombok.Data;

import java.util.List;

@Data
public class MovieDTO {
    private String title;
    private int year;
    private String rated;
    private String released;
    private String runtime;
    private String genre;
    private String director;
    private String writer;
    private String actors;
    private String plot;
    private String awards;
    private String poster;
    private List<RatingDTO> ratings;
    private int metascore;
    private float imdbrating;
    private String type;

    @Data
    private class RatingDTO {
        private String source;
        private String value;
    }
}
