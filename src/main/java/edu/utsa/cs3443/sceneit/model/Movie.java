package edu.utsa.cs3443.sceneit.model;

import java.util.List;
import java.time.LocalDate;

/** A movie entry that inherits from Media class */
public class Movie extends Media {

    public Movie(int id, LocalDate date, String title, int releaseYear, List<String> genres, WatchStatus watchStatus, Double rating, String notes) {
        super(id, date, title, releaseYear, genres, watchStatus, rating, notes);
    }


    @Override
    public String getType() {
        return "MOVIE";
    }
}
