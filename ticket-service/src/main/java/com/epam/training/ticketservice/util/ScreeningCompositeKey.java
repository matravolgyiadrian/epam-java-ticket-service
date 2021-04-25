package com.epam.training.ticketservice.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class ScreeningCompositeKey implements Serializable {
    private String movieTitle;
    private String roomName;
    private LocalDateTime startTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ScreeningCompositeKey that = (ScreeningCompositeKey) o;
        return Objects.equals(movieTitle, that.movieTitle)
                && Objects.equals(roomName, that.roomName)
                && Objects.equals(startTime, that.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieTitle, roomName, startTime);
    }
}
