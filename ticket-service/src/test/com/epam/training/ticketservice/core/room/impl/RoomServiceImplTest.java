package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;


class RoomServiceImplTest {

    private static final Room alpha = Room.builder()
            .name("Alpha")
            .rows(4)
            .columns(5)
            .build();

    private static final Room updatedAlpha = new Room("Alpha", 5, 5);

    private RoomRepository roomRepository;
    private ConsoleService console;
    private RoomServiceImpl underTest;

    @BeforeEach
    public void init(){
        roomRepository = Mockito.mock(RoomRepository.class);
        console = Mockito.mock(ConsoleService.class);
        underTest = new RoomServiceImpl(roomRepository, console);
    }

    @Test
    void testListAllShouldCallRoomRepository() {
        //Given

        //When
        underTest.listAll();

        //Then
        Mockito.verify(roomRepository).findAll();
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testListAllShouldPrintRooms() {
        //Given
        Mockito.when(roomRepository.findAll()).thenReturn(List.of(alpha));

        //When
        underTest.listAll();

        //Then
        Mockito.verify(console).print("Room Alpha with 20 seats, 4 rows and 5 columns");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testListAllShouldCallConsoleAndPrintErrorWhenListIsEmpty() {
        //Given
        Mockito.when(roomRepository.findAll()).thenReturn(List.of());

        //When
        underTest.listAll();

        //Then
        Mockito.verify(console).printError("There are no rooms at the moment");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallRoomRepositoryWhenTheInputIsValid() {
        //Given

        //When
        underTest.create("Alpha", 4, 5);

        //Then
        Mockito.verify(roomRepository).save(alpha);
        Mockito.verify(roomRepository).findById("Alpha");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenTheInputIsAlreadyExists() {
        //Given
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.create("Alpha", 4, 5);

        //Then
        Mockito.verify(console).printError("This room is already exists");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testUpdateShouldCallRoomRepositoryWhenInputIsValid() {
        //Given
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.update("Alpha", 5, 5);

        //Then
        Mockito.verify(roomRepository).save(updatedAlpha);
        Mockito.verify(roomRepository).findById("Alpha");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testUpdateShouldCallConsoleAndPrintErrorWhenRoomDoesntExists() {
        //Given
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        //When
        underTest.update("Alpha", 4, 5);

        //Then
        Mockito.verify(console).printError("There is no room with name: %s", "Alpha");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDeleteShouldCallRoomRepositoryWhenInputIsValid() {
        //Given
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.delete("Alpha");

        //Then
        Mockito.verify(roomRepository).findById("Alpha");
        Mockito.verify(roomRepository).deleteById("Alpha");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testDeleteShouldCallConsoleAndPrintErrorWhenRoomDoesntExists() {
        //Given
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        //When
        underTest.delete("Alpha");

        //Then
        Mockito.verify(console).printError("The room doesn't exist.");
        Mockito.verifyNoMoreInteractions(console);
    }
}