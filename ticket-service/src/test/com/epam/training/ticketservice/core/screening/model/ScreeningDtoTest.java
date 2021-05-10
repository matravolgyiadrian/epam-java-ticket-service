package com.epam.training.ticketservice.core.screening.model;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningDtoTest {


    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        ScreeningDto screeningDto1 = new ScreeningDto(new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10),
                LocalDateTime.of(2021,10,10,12,0));
        ScreeningDto screeningDto2 = new ScreeningDto(new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10),
                LocalDateTime.of(2021,10,10,12,0));

        Assertions.assertEquals(screeningDto1, screeningDto2);
        Assertions.assertTrue(screeningDto1.hashCode() == screeningDto2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        ScreeningDto screeningDto1 = new ScreeningDto(new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10),
                LocalDateTime.of(2021,10,10,12,0));
        ScreeningDto screeningDto2 = new ScreeningDto(new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10),
                LocalDateTime.of(2021,10,11,12,0));

        //When

        //Then
        Assertions.assertNotEquals(screeningDto1, screeningDto2);
        Assertions.assertFalse(screeningDto1.hashCode() == screeningDto2.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeNull(){
        //Given
        ScreeningDto screeningDto = new ScreeningDto();

        //When

        //Then
        Assertions.assertNotEquals(0, screeningDto.hashCode());
    }

}