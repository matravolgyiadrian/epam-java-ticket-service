package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.movie.impl.MovieServiceImpl;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.ui.valueproviders.MovieValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class MovieCommands {

    private final MovieServiceImpl movieService;
    private final UserServiceImpl userService;

    @Autowired
    public MovieCommands(MovieRepository movieRepository, MovieServiceImpl movieService, UserServiceImpl userService) {
        this.movieService = movieService;
        this.userService = userService;
    }

    @ShellMethod(value = "List movies.", key = "list movies")
    public void listMovies() {
        movieService.listAll();
    }

    @ShellMethod(value = "Create movie.", key = "create movie")
    public void create(String title, String genre, int length) {
        movieService.create(title, genre, length);
    }

    @ShellMethod(value = "Update existing movie.", key = "update movie")
    public void update(
            @ShellOption(valueProvider = MovieValueProvider.class) String title, String genre, int length) {
        movieService.update(title, genre, length);
    }

    @ShellMethod(value = "Delete a movie.", key = "delete movie")
    public void delete(
            @ShellOption(valueProvider = MovieValueProvider.class) String title) {
        movieService.delete(title);
    }


    @ShellMethodAvailability({"create movie", "update movie", "delete movie"})
    public Availability isAdmin() {
        return userService.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you have to be signed in as a privileged user.");
    }
}
