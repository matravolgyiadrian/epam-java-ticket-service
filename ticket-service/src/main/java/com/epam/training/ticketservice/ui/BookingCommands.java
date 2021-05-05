package com.epam.training.ticketservice.ui;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.shell.Availability;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.ParameterDescription;
import org.springframework.shell.ParameterResolver;
import org.springframework.shell.ValueResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@ShellComponent
public class BookingCommands {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingCommands(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @ShellMethod(value = "Book seats for screening.", key = "book")
    public void create(@ShellOption(optOut = true) List<String> arguments) {
        bookingService.create(arguments);
    }

    @Bean
    public ParameterResolver commandParameterResolver() {
        return new ParameterResolver() {

            @Override
            public boolean supports(MethodParameter parameter) {
                return parameter.getParameterType().isAssignableFrom(List.class);
            }

            @Override
            public ValueResult resolve(MethodParameter methodParameter, List<String> words) {
                return new ValueResult(methodParameter, words);
            }

            @Override
            public Stream<ParameterDescription> describe(MethodParameter parameter) {
                return Stream.of(ParameterDescription.outOf(parameter));
            }

            @Override
            public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext context) {
                return Collections.emptyList();
            }
        };
    }

    @ShellMethodAvailability("book")
    public Availability isSignedIn() {
        return userService.isSignedIn()
                ? Availability.available()
                : Availability.unavailable("you have to be signed in to book seats.");
    }
}
