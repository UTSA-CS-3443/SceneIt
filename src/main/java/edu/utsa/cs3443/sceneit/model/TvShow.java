package edu.utsa.cs3443.sceneit.model;

import java.util.List;
import java.time.LocalDate;

// A TV entry; each tracked season is a separate entry identified in its title
public class TvShow extends Media {
    // Constructor
    public TvShow(int id, LocalDate date, String title, int releaseYear, List<String> genres, WatchStatus watchStatus, Double rating, String notes) {
        super(id, date, title, releaseYear, genres, watchStatus, rating, notes);
    }

    // Return type of media (TvShow)
    @Override
    public String getType() {
        return "TV_SHOW";
    }
}
