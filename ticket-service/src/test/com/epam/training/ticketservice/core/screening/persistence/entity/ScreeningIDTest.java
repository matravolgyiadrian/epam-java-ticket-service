package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningIDTest {

    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        ScreeningID screeningId1 = new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,12,0));
        ScreeningID screeningId2 = new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,12,0));


        Assertions.assertEquals(screeningId1, screeningId2);
        Assertions.assertTrue(screeningId1.hashCode() == screeningId2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        ScreeningID screeningId1 = new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,12,0));
        ScreeningID screeningId2 = new ScreeningID("Transformers", "Alpha", LocalDateTime.of(2021,10,10,12,0));

        //When

        //Then
        Assertions.assertNotEquals(screeningId1, screeningId2);
        Assertions.assertFalse(screeningId1.hashCode() == screeningId2.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeNull(){
        //Given
        ScreeningID screeningId = new ScreeningID();

        //When

        //Then
        Assertions.assertNotEquals(0, screeningId.hashCode());
    }


}