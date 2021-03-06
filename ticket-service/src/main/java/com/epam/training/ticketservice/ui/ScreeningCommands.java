package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.ui.valueproviders.MovieValueProvider;
import com.epam.training.ticketservice.ui.valueproviders.RoomValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class ScreeningCommands {

    private final ScreeningServiceImpl screeningService;
    private final UserServiceImpl userService;

    @Autowired
    public ScreeningCommands(ScreeningServiceImpl screeningService, UserServiceImpl userService) {
        this.screeningService = screeningService;
        this.userService = userService;
    }

    @ShellMethod(value = "Create screening.", key = "create screening")
    public void create(
            @ShellOption(valueProvider = MovieValueProvider.class) String movieTitle,
            @ShellOption(valueProvider = RoomValueProvider.class) String roomName, String start) {
        screeningService.create(movieTitle, roomName, start);
    }

    @ShellMethod(value = "List screenings.", key = "list screenings")
    public void list() {
        screeningService.listAll();
    }

    @ShellMethod(value = "Delete screening.", key = "delete screening")
    public void delete(String movieTitle, String roomName, String start) {
        screeningService.delete(movieTitle, roomName, start);
    }


    @ShellMethodAvailability({"create screening", "delete screening"})
    public Availability isAdmin() {
        return userService.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you have to be signed in as a privileged user.");
    }
}
