package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.ui.valueproviders.MovieValueProvider;
import com.epam.training.ticketservice.ui.valueproviders.RoomValueProvider;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class BookingCommands {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingCommands(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @ShellMethod(value = "Book seats for screening.", key = "book")
    public void create(
            @ShellOption(valueProvider = MovieValueProvider.class) String movieTitle,
            @ShellOption(valueProvider = RoomValueProvider.class) String roomName, String start, String seatsString) {
        bookingService.create(movieTitle, roomName, start, seatsString);
    }

    @ShellMethodAvailability("book")
    public Availability isSignedIn() {
        return userService.isSignedIn()
                ? Availability.available()
                : Availability.unavailable("you have to be signed in to book seats.");
    }
}
