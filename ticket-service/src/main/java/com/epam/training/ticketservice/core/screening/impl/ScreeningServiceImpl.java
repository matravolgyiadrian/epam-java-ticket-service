package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.ScreeningService;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningCompositeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final RoomRepository roomRepository;
    private final MovieRepository movieRepository;
    private final ConsoleService console;

    @Autowired
    public ScreeningServiceImpl(ScreeningRepository screeningRepository,
                                RoomRepository roomRepository,
                                MovieRepository movieRepository, ConsoleService console) {
        this.screeningRepository = screeningRepository;
        this.roomRepository = roomRepository;
        this.movieRepository = movieRepository;
        this.console = console;
    }


    @Override
    public void listAll() {
        if (screeningRepository.findAll().isEmpty()) {
            console.printError("There are no screenings");
        } else {
            screeningRepository.findAll()
                    .stream()
                    .map(screening -> new ScreeningDto(
                            movieRepository.findById(screening.getMovieTitle()).get(),
                            roomRepository.findById(screening.getRoomName()).get(),
                            screening.getStartTime()
                    ).toString())
                    .forEach(console::print);
        }
    }

    @Override
    public void create(String movieTitle, String roomName, String start) {
        try {
            Movie movie = movieRepository.findById(movieTitle)
                    .orElseThrow(() -> new NoSuchElementException("The movie " + movieTitle + " doesn't exists"));
            Room room = roomRepository.findById(roomName)
                    .orElseThrow(() -> new NoSuchElementException("The room " + roomName + " doesn't exists"));
            LocalDateTime startTime = convertStringToLocalDateTime(start);

            if (screeningRepository.findById(new ScreeningCompositeKey(movieTitle, roomName, startTime)).isPresent()) {
                throw new InstanceAlreadyExistsException("This screening is already exists");
            }

            saveScreening(movie, room, startTime);

            log.debug("Screening created with movie title: {}, room name: {}, start time: {}",
                    movieTitle, roomName, start);
        } catch (Exception e) {
            console.printError(e.getMessage());
        }
    }

    @Override
    public void delete(String movieTitle, String roomName, String start) {
        try {
            LocalDateTime startTime = convertStringToLocalDateTime(start);
            ScreeningCompositeKey key = new ScreeningCompositeKey(movieTitle, roomName, startTime);
            if (screeningRepository.findById(key).isPresent()) {
                screeningRepository.deleteById(key);

                log.debug("Screening deleted with movie title: {}, room name: {}, start time: {}",
                        movieTitle, roomName, start);
            } else {
                console.printError("This screening doesn't exists");
            }

        } catch (Exception e) {
            console.printError(e.getMessage());
        }
    }

    private void saveScreening(Movie movie, Room room, LocalDateTime startTime) {
        List<ScreeningDto> screenings = getFilteredScreeningObjects(room.getName(), startTime);
        String overlapStatus = getOverlapStatus(screenings, movie, startTime);

        if (overlapStatus != null) {
            console.printError(overlapStatus);
        } else {
            screeningRepository.save(Screening.builder()
                    .movieTitle(movie.getTitle())
                    .roomName(room.getName())
                    .startTime(startTime)
                    .build());
        }
    }

    private String getOverlapStatus(List<ScreeningDto> list, Movie movie, LocalDateTime startTime) {
        for (ScreeningDto screening : list) {
            LocalTime startA = startTime.toLocalTime();
            LocalTime startB = screening.getStart().toLocalTime();
            LocalTime stopA = startTime.plusMinutes(movie.getLength()).toLocalTime();
            LocalTime stopB = screening.getStart().plusMinutes(screening.getMovie().getLength()).toLocalTime();

            if (startA.isBefore(stopB) && stopA.isAfter(startB)) {
                return "There is an overlapping screening";
            }

            if (startA.isBefore(stopB.plusMinutes(10)) && stopA.plusMinutes(10).isAfter(startB)) {
                return "This would start in the break period after another screening in this room";
            }
        }

        return null;
    }

    private List<ScreeningDto> getFilteredScreeningObjects(String roomName, LocalDateTime startTime) {
        return screeningRepository.findAll()
                .stream()
                .filter(screening -> screening.getStartTime().getYear() == startTime.getYear()
                        && screening.getStartTime().getMonthValue() == startTime.getMonthValue()
                        && screening.getStartTime().getDayOfMonth() == startTime.getDayOfMonth()
                        && screening.getRoomName().equals(roomName))
                .map(screening -> new ScreeningDto(
                        movieRepository.findById(screening.getMovieTitle()).get(),
                        roomRepository.findById(screening.getRoomName()).get(),
                        screening.getStartTime()))
                .collect(Collectors.toList());
    }

    private LocalDateTime convertStringToLocalDateTime(String time) throws IllegalArgumentException {
        if (!time.matches("[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}")) {
            throw new IllegalArgumentException(
                    "The start time has to match the required format! ( YYYY-MM-DD hh:mm )");
        }
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
