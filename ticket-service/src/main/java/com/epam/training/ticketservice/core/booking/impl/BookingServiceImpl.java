package com.epam.training.ticketservice.core.booking.impl;

import com.epam.training.ticketservice.core.booking.BookingService;
import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


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
    public void create(List<String> args) {
        try {
            String movieTitle = args.get(0);
            String roomName = args.get(1);
            String start = args.get(2);
            ScreeningDto screening = screeningService.convertToScreeningDto(movieTitle, roomName, start);
            if (args.size() < 3) {
                throw new IllegalArgumentException("You have to follow this format: "
                        + "book <movie title> <room name> <seats (row, column) separated by spaces>");
            }
            if (args.size() == 3) {
                throw new IllegalArgumentException("You have to book at least one seat.");
            }

            Set<Seat> seats = getSeatsForScreening(screening, args);

            ScreeningID screeningID = new ScreeningID(movieTitle, roomName, screening.getStart());
            checkTakenSeats(screeningID, screening.getRoom(), seats);
            bookingRepository.save(Booking.builder()
                    .screening(screeningID)
                    .username(userService.getLoggedInUser())
                    .seats(seats)
                    .build());


        } catch (Exception e) {
            console.printError(e.getMessage());
        }
    }

    private Set<Seat> getSeatsForScreening(ScreeningDto screening, List<String> args){
        Set<Seat> seats = new HashSet<>();

        for (int i = 3; i < args.size(); i++) {
            String temp = args.get(i);
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
        Pattern pattern = Pattern.compile("\\((\\d+), (\\d+)\\)");
        Matcher matcher = pattern.matcher(temp);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("You have to specify seats in the format of (<row>, <column>)");
        }
        int row = Integer.parseInt(matcher.group(1));
        int column = Integer.parseInt(matcher.group(2));

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
