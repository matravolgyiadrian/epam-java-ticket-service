package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningTest {

    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        Screening screening1 = new Screening(new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,12,0)),
                new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10));
        Screening screening2 = new Screening(new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,12,0)),
                new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10));


        Assertions.assertEquals(screening1, screening2);
        Assertions.assertTrue(screening1.hashCode() == screening2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        Screening screening1 = new Screening(new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2020,10,10,12,0)),
                new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10));
        Screening screening2 = new Screening(new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,12,0)),
                new Movie("Terminator", "action", 120),
                new Room("Alpha", 10, 10));

        //When

        //Then
        Assertions.assertNotEquals(screening1, screening2);
        Assertions.assertFalse(screening1.hashCode() == screening2.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeNull(){
        //Given
        Screening screening = new Screening();

        //When

        //Then
        Assertions.assertNotEquals(0, screening.hashCode());
    }


}