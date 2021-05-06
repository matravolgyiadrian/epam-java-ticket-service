package com.epam.training.ticketservice.core.movie;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;

import java.util.Collection;

public interface MovieService {
    void listAll();

    void create(String title, String genre, int length);

    void update(String title, String genre, int length);

    void delete(String title);

    Collection<Movie> findByTitle(String title);
}
