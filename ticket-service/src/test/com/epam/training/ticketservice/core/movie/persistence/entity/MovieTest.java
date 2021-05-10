package com.epam.training.ticketservice.core.movie.persistence.entity;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        Movie movie1 = new Movie("Terminator", "action", 120);
        Movie movie2 = new Movie("Terminator", "action", 120);

        Assertions.assertEquals(movie1, movie2);
        Assertions.assertTrue(movie1.hashCode() == movie2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        //Given
        Movie movie1 = new Movie("Terminator", "action", 120);
        Movie movie2 = new Movie("Transformers", "action", 140);

        //When

        //Then
        Assertions.assertNotEquals(movie1, movie2);
        Assertions.assertFalse(movie1.hashCode() == movie2.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeNull(){
        //Given
        Movie movie = new Movie();

        //When

        //Then
        Assertions.assertNotEquals(0, movie.hashCode());
    }


}