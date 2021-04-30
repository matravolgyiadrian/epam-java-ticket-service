package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.MovieService;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;
    private final ConsoleService console;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ConsoleService consoleService) {
        this.movieRepository = movieRepository;
        this.console = consoleService;
    }

    @Override
    public void listAll() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            console.printError("There are no movies at the moment");
        } else {
            movies.stream()
                    .map(Movie::toString)
                    .forEach(console::print);
        }

    }

    @Override
    public void create(String title, String genre, int length) {
        if (movieRepository.findById(title).isPresent()) {
            console.printError("This movie is already in our database.");
        } else {
            movieRepository.save(
                    Movie.builder()
                            .title(title)
                            .genre(genre)
                            .length(length)
                            .build());

            log.debug("Movie created with title: {}, genre: {}, length: {}", title, genre, length);
        }
    }

    @Override
    public void update(String title, String genre, int length) {
        if (movieRepository.findById(title).isEmpty()) {
            console.printError("There is no movie with title: %s", title);
        } else {
            movieRepository.save(
                    Movie.builder()
                            .title(title)
                            .genre(genre)
                            .length(length)
                            .build());

            log.debug("Movie updated with title: {}, genre: {}, length: {}", title, genre, length);
        }
    }

    @Override
    public void delete(String title) {
        if (movieRepository.findById(title).isPresent()) {
            movieRepository.deleteById(title);

            log.debug("Movie deleted, with title: {}", title);
        } else {
            console.printError("The movie doesn't exist.");
        }
    }

    @Override
    public Collection<Movie> findByTitle(String title) {
        return this.movieRepository.findAll()
                .stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
}
