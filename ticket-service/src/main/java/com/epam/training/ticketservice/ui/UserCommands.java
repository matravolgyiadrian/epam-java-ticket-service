package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
public class UserCommands {
    private final UserService userService;

    @Autowired
    public UserCommands(UserService userService) {
        this.userService = userService;
    }

    @ShellMethodAvailability("signedInAvailability")
    @ShellMethod(value = "Sign in to the ticket service system.", key = "sign in privileged")
    public void signIn(String username, String password) {
        userService.signIn(username, password);
    }

    @ShellMethodAvailability("signedOutAvailability")
    @ShellMethod(value = "Sign out from the ticket service system.", key = "sign out")
    public void signOut() {
        userService.signOut();
    }

    @ShellMethod(value = "Shows the currently logged in account type and condition.", key = "describe account")
    public void describeAccount() {
        userService.describe();
    }


    public Availability signedOutAvailability() {
        return userService.isSignedIn()
                ? Availability.available()
                : Availability.unavailable("you have to be signed in to use this command.");
    }

    public Availability signedInAvailability() {
        return !userService.isSignedIn()
                ? Availability.available()
                : Availability.unavailable("you are already signed in.");
    }
}
