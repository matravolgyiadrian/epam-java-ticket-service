package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.core.room.impl.RoomServiceImpl;
import com.epam.training.ticketservice.core.user.impl.UserServiceImpl;
import com.epam.training.ticketservice.ui.valueproviders.RoomValueProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class RoomCommands {

    private final RoomServiceImpl roomService;
    private final UserServiceImpl userService;

    @Autowired
    public RoomCommands(RoomServiceImpl roomService, UserServiceImpl userService) {
        this.roomService = roomService;
        this.userService = userService;
    }

    @ShellMethod(value = "Lists rooms", key = "list rooms")
    public void list() {
        roomService.listAll();
    }

    @ShellMethod(value = "Create a room", key = "create room")
    public void create(String name, int rows, int columns) {
        roomService.create(name, rows, columns);
    }

    @ShellMethod(value = "Update existing room", key = "update room")
    public void update(
            @ShellOption(valueProvider = RoomValueProvider.class) String name, int rows, int columns) {
        roomService.update(name, rows, columns);
    }

    @ShellMethod(value = "Delete room", key = "delete room")
    public void delete(
            @ShellOption(valueProvider = RoomValueProvider.class) String name) {
        roomService.delete(name);
    }


    @ShellMethodAvailability({"create room", "update room", "delete room"})
    public Availability isSignedIn() {
        return userService.isAdmin()
                ? Availability.available()
                : Availability.unavailable("you have to be signed in as a privileged user.");
    }
}
