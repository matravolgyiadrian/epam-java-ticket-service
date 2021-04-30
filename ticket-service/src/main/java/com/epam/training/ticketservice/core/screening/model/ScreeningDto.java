package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningDto {
    private Movie movie;
    private Room room;
    private LocalDateTime start;

    public String toString() {
        String formattedStartTime = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return String.format("%s (%s, %d minutes), screened in room %s, at %s",
                movie.getTitle(), movie.getGenre(), movie.getLength(), room.getName(), formattedStartTime);
    }
}
