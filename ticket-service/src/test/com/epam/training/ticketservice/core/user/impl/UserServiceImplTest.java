package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import com.epam.training.ticketservice.core.user.persistence.entity.AccountType;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {

    private static final User user = User.builder()
            .username("alma")
            .password("alma")
            .type(AccountType.USER)
            .build();

    private static final User admin = User.builder()
            .username("admin")
            .password("admin")
            .type(AccountType.ADMIN)
            .build();

    private UserServiceImpl underTest;

    private UserRepository userRepository;
    private BookingRepository bookingRepository;
    private ConsoleService console;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        console = Mockito.mock(ConsoleService.class);

        underTest = new UserServiceImpl(userRepository, console, bookingRepository);
    }

    @Test
    void testIsSignedInShouldReturnTrueWhenUserSignedIn() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        //When
        underTest.signIn("alma", "alma", false);

        //Then
        Assertions.assertTrue(underTest.isSignedIn());
    }

    @Test
    void testIsSignedInShouldReturnFalseWhenUserSignedIn() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        //When

        //Then
        Assertions.assertFalse(underTest.isSignedIn());
    }

    @Test
    void testIsAdminInShouldReturnTrueWhenAdminUserSignedIn() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(admin));

        //When
        underTest.signIn("admin", "admin", true);

        //Then
        Assertions.assertTrue(underTest.isAdmin());
    }

    @Test
    void testIsAdminInShouldReturnFalseWhenAdminUserSignedIn() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(admin));

        //When

        //Then
        Assertions.assertFalse(underTest.isAdmin());
    }

    @Test
    void testSignUpShouldCallUserRepositoryWhenInputValid() {
        //Given

        //When
        underTest.signUp("alma", "alma");

        //Then
        Mockito.verify(userRepository).findAll();
        Mockito.verify(userRepository).save(user);
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    void testSignUpShouldCallConsoleAndPrintErrorWhenUserAlreadyExists() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        //When
        underTest.signUp("alma", "alma");

        //Then
        Mockito.verify(console).printError("This username is already taken.");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testSignInShouldSetLoggedInUserWhenUserSignIn() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        //When
        underTest.signIn("alma", "alma", false);

        //Then
        Assertions.assertEquals("alma", underTest.getLoggedInUser());
    }

    @Test
    void testSignInShouldCallConsoleAndPrintErrorWhenTheCredentialsIncorrect() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        //When
        underTest.signIn("alma", "korte", false);

        //Then
        Mockito.verify(console).printError("Login failed due to incorrect credentials");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testSignOutShouldChangeIsSignedInToFalse() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        underTest.signIn("alma", "alma", false);

        //When
        underTest.signOut();

        //Then
        Assertions.assertFalse(underTest.isSignedIn());
    }

    @Test
    void testDescribeShouldCallConsoleAndPrintErrorWhenNoUserSignedIn() {
        //Given

        //When
        underTest.describe();

        //Then
        Mockito.verify(console).printError("You are not signed in");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDescribeShouldCallBookingRepositoryWhenUserSignedIn() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        underTest.signIn("alma", "alma", false);

        //When
        underTest.describe();

        //Then
        Mockito.verify(bookingRepository).findByUsername("alma");
        Mockito.verifyNoMoreInteractions(bookingRepository);
    }

    @Test
    void testDescribeShouldCallConsoleAndPrintUserDataWhenAdminSignedInAndHaveNoBooking() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(admin));
        underTest.signIn("admin", "admin", true);

        //When
        underTest.describe();

        //Then
        Mockito.verify(console, Mockito.times(2)).print("Signed in with privileged account '%s'", "admin");
        Mockito.verify(console).print("%s", "You have not booked any tickets yet");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDescribeShouldCallConsoleAndPrintUserDataWhenUserSignedInAndHaveNoBooking() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        underTest.signIn("alma", "alma", false);

        //When
        underTest.describe();

        //Then
        Mockito.verify(console).print("Signed in with account '%s'", "alma");
        Mockito.verify(console).print("%s", "You have not booked any tickets yet");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDescribeShouldCallConsoleAndPrintUserDataWhenAdminSignedInAndHaveBooking() {
        //Given
        ScreeningID screeningID = new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021, 11, 1, 12, 0));
        Booking booking = Booking.builder().id(1L).username("admin").screening(screeningID).seats(Set.of(new Seat(2, 3))).build();
        Mockito.when(userRepository.findAll()).thenReturn(List.of(admin));
        Mockito.when(bookingRepository.findByUsername(Mockito.anyString())).thenReturn(List.of(booking));

        underTest.signIn("admin", "admin", true);

        //When
        underTest.describe();

        //Then
        Mockito.verify(console, Mockito.times(2)).print("Signed in with privileged account '%s'", "admin");
        Mockito.verify(console).print("%s", "Your previous bookings are\n" +
                "Seats (2,3) on Terminator in room Alpha starting at 2021-11-01 12:00 for 1500 HUF");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDescribeShouldCallConsoleAndPrintUserDataWhenUserSignedInAndHaveBooking() {
        //Given
        ScreeningID screeningID = new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021, 11, 1, 12, 0));
        Booking booking = Booking.builder().id(1L).username("alma").screening(screeningID).seats(Set.of(new Seat(2, 3))).build();
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        Mockito.when(bookingRepository.findByUsername(Mockito.anyString())).thenReturn(List.of(booking));

        underTest.signIn("alma", "alma", false);

        //When
        underTest.describe();

        //Then
        Mockito.verify(console).print("Signed in with account '%s'", "alma");
        Mockito.verify(console).print("%s", "Your previous bookings are\n" +
                "Seats (2,3) on Terminator in room Alpha starting at 2021-11-01 12:00 for 1500 HUF");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testGetLoggedInUserShouldReturnLoggedInUserName() {
        //Given
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));

        //When
        underTest.signIn("alma", "alma", false);

        //Then
        Assertions.assertEquals("alma", underTest.getLoggedInUser());
    }
}