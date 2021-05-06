package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ScreeningService screeningService;
    private final ConsoleService console;
    private final UserService userService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              ScreeningService screeningService,
                              ConsoleService console,
                              UserService userService) {
        this.bookingRepository = bookingRepository;
        this.screeningService = screeningService;
        this.console = console;
        this.userService = userService;
    }

    @Override
    public void create(String movieTitle, String roomName, String start, String seatsString) {
        try {
            ScreeningDto screening = screeningService.convertToScreeningDto(movieTitle, roomName, start);
            if (seatsString.isEmpty()) {
                throw new IllegalArgumentException("You have to book at least one seat.");
            }

            Set<Seat> seats = getSeatsForScreening(screening, seatsString);

            ScreeningID screeningID = new ScreeningID(movieTitle, roomName, screening.getStart());
            checkTakenSeats(screeningID, screening.getRoom(), seats);
            bookingRepository.save(Booking.builder()
                    .screening(screeningID)
                    .username(userService.getLoggedInUser())
                    .seats(seats)
                    .build());

            console.print("Seats booked: %s; the price for this booking is %s HUF",
                    seats.toString().replace("[", "").replace("]", ""),
                    String.valueOf(seats.size() * 1500));
        } catch (Exception e) {
            console.printError(e.getMessage());
        }
    }

    private Set<Seat> getSeatsForScreening(ScreeningDto screening, String seatsString) {
        Set<Seat> seats = new HashSet<>();

        for (String temp : seatsString.split(" ")) {
            Seat seat = convertToSeat(temp);

            if (seat.getSeatRow() < 1 || seat.getSeatRow() > screening.getRoom().getRows()
                    || seat.getSeatColumn() < 1 || seat.getSeatColumn() > screening.getRoom().getColumns()) {
                throw new IllegalArgumentException("Seat " + seat + " does not exist in this room");
            }
            seats.add(seat);
        }
        return seats;
    }

    private Seat convertToSeat(String temp) {
        String[] numbers = temp.split(",");
        if (numbers.length != 2) {
            throw new IllegalArgumentException("You have to specify seats in the format of \"<row>,<column>\"");
        }
        int row = Integer.parseInt(numbers[0]);
        int column = Integer.parseInt(numbers[1]);

        return Seat.builder()
                .seatRow(row)
                .seatColumn(column)
                .build();
    }

    private void checkTakenSeats(ScreeningID screeningID, Room room, Set<Seat> seats)
            throws InstanceAlreadyExistsException {
        List<Booking> bookings = bookingRepository.findByScreening(screeningID);
        Set<Seat> takenSeats = new HashSet<>();
        for (Booking booking : bookings) {
            takenSeats.addAll(booking.getSeats());
        }
        if (takenSeats.size() == room.getNumberOfSeats()) {
            throw new RuntimeException("All the seats are booked to this screening.");
        }
        Optional<Seat> takenSeat = seats.stream()
                .filter(takenSeats::contains)
                .findFirst();
        if (takenSeat.isPresent()) {
            throw new InstanceAlreadyExistsException("Seat " + takenSeat.get() + " is already taken");
        }
    }


}
