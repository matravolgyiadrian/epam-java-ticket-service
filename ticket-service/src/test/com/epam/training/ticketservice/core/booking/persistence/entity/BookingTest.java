package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import com.epam.training.ticketservice.core.user.persistence.entity.AccountType;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        Booking booking1 = new Booking(1L,
                "alma",
                new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,11,0)),
                Set.of(new Seat(1, 2)));
        Booking booking2 = new Booking(1L,
                "alma",
                new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,11,0)),
                Set.of(new Seat(1, 2)));

        Assertions.assertEquals(booking1, booking2);
        Assertions.assertTrue(booking1.hashCode() == booking2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        //Given
        Booking booking1 = new Booking(1L,
                "alma",
                new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,11,0)),
                Set.of(new Seat(1, 2)));
        Booking booking2 = new Booking(1L,
                "korte",
                new ScreeningID("Terminator", "Alpha", LocalDateTime.of(2021,10,10,11,0)),
                Set.of(new Seat(1, 2)));

        //When

        //Then
        Assertions.assertNotEquals(booking1, booking2);
        Assertions.assertFalse(booking1.hashCode() == booking2.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeNull(){
        //Given
        Booking booking = new Booking();

        //When

        //Then
        Assertions.assertNotEquals(0, booking.hashCode());
    }


}