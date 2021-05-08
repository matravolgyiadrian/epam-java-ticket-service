package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.impl.ScreeningServiceImpl;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceImplTest {

    private BookingServiceImpl underTest;

    private ScreeningRepository screeningRepository;
    private RoomRepository roomRepository;
    private MovieRepository movieRepository;

    private BookingRepository bookingRepository;
    private ScreeningService screeningService;
    private ConsoleService console;
    private UserService userService;

    @BeforeEach
    public void init(){
        bookingRepository = Mockito.mock(BookingRepository.class);
        screeningService = Mockito.mock(ScreeningServiceImpl.class);

        console = Mockito.mock(ConsoleService.class);
        userService = Mockito.mock(UserService.class);

        underTest = new BookingServiceImpl(bookingRepository, screeningService, console, userService);
    }

    @Test
    void testCreateShouldCallScreeningServiceWhenTheInputIsValid() {
        //Given


        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "1,1 2,2");

        //Then
        Mockito.verify(screeningService).convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00");
        Mockito.verifyNoMoreInteractions(screeningService);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenNoSeatWereGiven() {
        //Given


        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "");

        //Then
        Mockito.verify(console).printError("You have to book at least one seat.");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsInWrongFormat() {
        //Given


        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "1, 2");

        //Then
        Mockito.verify(console).printError("You have to specify seats in the format of \"<row>,<column>\"");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsDoesntExistInTheRoomNo1() {
        //Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(5).columns(5).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "0,0");

        //Then
        Mockito.verify(console).printError("Seat (0,0) does not exist in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsDoesntExistInTheRoomNo2() {
        //Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(5).columns(5).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "1,0");

        //Then
        Mockito.verify(console).printError("Seat (1,0) does not exist in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsDoesntExistInTheRoomNo3() {
        //Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(5).columns(5).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "0,1");

        //Then
        Mockito.verify(console).printError("Seat (0,1) does not exist in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsDoesntExistInTheRoomNo4() {
        //Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(5).columns(5).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "6,6");

        //Then
        Mockito.verify(console).printError("Seat (6,6) does not exist in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsDoesntExistInTheRoomNo5() {
        //Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(5).columns(5).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "6,5");

        //Then
        Mockito.verify(console).printError("Seat (6,5) does not exist in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenSeatsDoesntExistInTheRoomNo6() {
        //Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(5).columns(5).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "5,6");

        //Then
        Mockito.verify(console).printError("Seat (5,6) does not exist in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenAllSeatsBooked() {
//        Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(1).columns(2).build())
                        .build());

        ScreeningID id = new ScreeningID("Terminator","Alpha", LocalDateTime.of(2021,11,1,12,0));
        Booking booking = Booking.builder().username("alma").screening(id).seats(Set.of(new Seat(1, 1), new Seat(1, 2))).build();
        Mockito.when(bookingRepository.findByScreening(Mockito.any(ScreeningID.class))).thenReturn(List.of(booking));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "1,1");

        //Then
        Mockito.verify(console).printError("All the seats are booked to this screening.");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenTheSeatAlreadyTaken() {
//        Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(1).columns(2).build())
                        .build());

        ScreeningID id = new ScreeningID("Terminator","Alpha", LocalDateTime.of(2021,11,1,12,0));
        Booking booking = Booking.builder().username("alma").screening(id).seats(Set.of(new Seat(1, 1))).build();
        Mockito.when(bookingRepository.findByScreening(Mockito.any(ScreeningID.class))).thenReturn(List.of(booking));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "1,1");

        //Then
        Mockito.verify(console).printError("Seat (1,1) is already taken");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintBookedSeatsWhenInputValid() {
//        Given
        Mockito.when(screeningService.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"))
                .thenReturn(ScreeningDto.builder()
                        .movie(Movie.builder().title("Terminator").genre("action").length(120).build())
                        .room(Room.builder().name("Alpha").rows(1).columns(2).build())
                        .build());

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00", "1,1");

        //Then
        Mockito.verify(console).print(
                "Seats booked: %s; the price for this booking is %s HUF", "(1,1)", "1500");
        Mockito.verifyNoMoreInteractions(console);
    }
}