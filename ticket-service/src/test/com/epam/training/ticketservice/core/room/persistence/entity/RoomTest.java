package com.epam.training.ticketservice.core.room.persistence.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    private static Room underTest;

    @Test
    void testRoomShouldReturnEmptyRoomObject(){
        //Given

        //When
        underTest = new Room();

        //Then
        Assertions.assertTrue(underTest.getName() == null);
        Assertions.assertTrue(underTest.getColumns() == 0);
        Assertions.assertTrue(underTest.getRows() == 0);
    }

    @Test
    void testSetName(){
        //Given
        underTest = new Room();

        //When
        underTest.setName("Alpha");

        //Then
        Assertions.assertTrue(underTest.getName().equals("Alpha"));
    }

    @Test
    void testSetRows(){
        //Given
        underTest = new Room();

        //When
        underTest.setRows(3);

        //Then
        Assertions.assertEquals(3, underTest.getRows());
    }

    @Test
    void testSetColumns(){
        //Given
        underTest = new Room();

        //When
        underTest.setColumns(3);

        //Then
        Assertions.assertEquals(3, underTest.getColumns());
    }


    @Test
    void testEqualsAndHashCodeShouldReturnTrue(){
        //Given
        Room room1 = new Room("Alpha", 2, 3);
        Room room2 = new Room("Alpha", 2, 3);

        Assertions.assertEquals(room1, room2);
        Assertions.assertTrue(room1.hashCode() == room2.hashCode());
    }
    @Test
    void testEqualsAndHashCodeShouldReturnFalse(){
        //Given
        Room room = new Room("Alpha", 2, 3);
        Room room1 = new Room("Beta", 2, 3);

        Assertions.assertNotEquals(room, room1);
        Assertions.assertFalse(room.hashCode() == room1.hashCode());
    }

}