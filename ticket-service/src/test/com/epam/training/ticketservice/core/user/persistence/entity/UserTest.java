package com.epam.training.ticketservice.core.user.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        User user1 = new User("alma", "alma", AccountType.USER);
        User user2 = new User("alma", "alma", AccountType.USER);

        Assertions.assertEquals(user1, user2);
        Assertions.assertTrue(user1.hashCode() == user2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        //Given
        User user1 = new User("alma", "alma", AccountType.USER);
        User user2 = new User("korte", "korte", AccountType.USER);

        //When

        //Then
        Assertions.assertNotEquals(user1, user2);
        Assertions.assertFalse(user1.hashCode() == user2.hashCode());
    }

    @Test
    void testHashCodeShouldNotBeNull(){
        //Given
        User user = new User();

        //When

        //Then
        Assertions.assertNotEquals(0, user.hashCode());
    }

}