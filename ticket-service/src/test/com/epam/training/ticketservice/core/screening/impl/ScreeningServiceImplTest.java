package com.epam.training.ticketservice.core.screening.impl;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import com.epam.training.ticketservice.core.screening.persistence.repository.ScreeningRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ScreeningServiceImplTest {

    private static final Movie terminator = Movie.builder()
            .title("Terminator")
            .genre("action")
            .length(120)
            .build();

    private static final Room alpha = Room.builder()
            .name("Alpha")
            .rows(4)
            .columns(5)
            .build();

    private static final ScreeningID screeningID = new ScreeningID("Terminator",
            "Alpha",
            LocalDateTime.of(2021, 11, 1, 12, 0));
    private static final ScreeningID overlapScreeningID = new ScreeningID("Terminator",
            "Alpha",
            LocalDateTime.of(2021, 11, 1, 11, 0));
    private static final ScreeningID secondOverlapScreeningID = new ScreeningID("Terminator",
            "Alpha",
            LocalDateTime.of(2021, 11, 1, 14, 5));

    private static final Screening screening = Screening.builder()
            .id(screeningID)
            .movie(terminator)
            .room(alpha)
            .build();

    private static final ScreeningDto screeningDto = ScreeningDto.builder()
            .movie(terminator)
            .room(alpha)
            .start(LocalDateTime.of(2021, 11, 1, 12, 0))
            .build();

    private static final Screening overlapScreening = new Screening(overlapScreeningID, terminator, alpha);
    private static final Screening secondOverlapScreening = new Screening(secondOverlapScreeningID, terminator, alpha);

    private ScreeningServiceImpl underTest;
    private ScreeningRepository screeningRepository;
    private RoomRepository roomRepository;
    private MovieRepository movieRepository;
    private ConsoleService console;

    @BeforeEach
    public void init(){
        screeningRepository = Mockito.mock(ScreeningRepository.class);
        roomRepository = Mockito.mock(RoomRepository.class);
        movieRepository = Mockito.mock(MovieRepository.class);
        console = Mockito.mock(ConsoleService.class);

        underTest = new ScreeningServiceImpl(screeningRepository,
                roomRepository,
                movieRepository,
                console);
    }

    @Test
    void testListAllShouldCallScreeningRepository() {
        //Given

        //When
        underTest.listAll();

        //Then
        Mockito.verify(screeningRepository).findAll();
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void testListAllShouldPrintMovies() {
        //Given
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of(screening));

        //When
        underTest.listAll();

        //Then
        Mockito.verify(console).print("Terminator (action, 120 minutes), screened in room Alpha, at 2021-11-01 12:00");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testListAllShouldCallConsoleAndPrintErrorWhenListIsEmpty() {
        //Given
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of());

        //When
        underTest.listAll();

        //Then
        Mockito.verify(console).printError("There are no screenings");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallMovieRepositoryWhenTheInputIsValid() {
        //Given

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(movieRepository).findById("Terminator");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testCreateShouldCallRoomRepositoryWhenTheInputIsValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(roomRepository).findById("Alpha");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testCreateShouldCallScreeningRepositoryWhenTheInputIsValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(screeningRepository).findById(screeningID);
        Mockito.verify(screeningRepository).findAll();
        Mockito.verify(screeningRepository).save(screening);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void testCreateShouldCallConsoleServiceAndPrintErrorWhenStartTimeNotValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.create("Terminator", "Alpha", "2021.11.01 12:00");

        //Then
        Mockito.verify(console).printError("The start time has to match the required format! ( YYYY-MM-DD hh:mm )");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleServiceAndPrintErrorWhenMovieDoesntExists() {
        //Given

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(console).printError("The movie Terminator doesn't exists");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleServiceAndPrintErrorWhenRoomDoesntExists() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(console).printError("The room Alpha doesn't exists");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleServiceAndPrintErrorWhenScreeningAlreadyExists() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));
        Mockito.when(screeningRepository.findById(screeningID)).thenReturn(Optional.of(screening));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(console).printError("This screening is already exists");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleServiceAndPrintErrorWhenTheStartTimeOverlapWithAnotherOneScreeningTime() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of(overlapScreening));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(console).printError("There is an overlapping screening");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallConsoleServiceAndPrintErrorWhenTheStartTimeOverlapWithAnotherOneBreakTime() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));
        Mockito.when(screeningRepository.findAll()).thenReturn(List.of(secondOverlapScreening));

        //When
        underTest.create("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(console).printError("This would start in the break period after another screening in this room");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDeleteShouldCallScreeningRepositoryWhenInputIsValid() {
        //Given
        Mockito.when(screeningRepository.findById(Mockito.any(ScreeningID.class))).thenReturn(Optional.of(screening));

        //When
        underTest.delete("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(screeningRepository).deleteById(screeningID);
        Mockito.verify(screeningRepository).findById(screeningID);
        Mockito.verifyNoMoreInteractions(screeningRepository);
    }

    @Test
    void testDeleteShouldCallConsoleAndPrintErrorWhenMovieDoesntExists() {
        //Given
        Mockito.when(screeningRepository.findById(Mockito.any(ScreeningID.class))).thenReturn(Optional.empty());

        //When
        underTest.delete("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(console).printError("This screening doesn't exists");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDeleteShouldCallConsoleAndPrintErrorWhenTimeFormatNotValid() {
        //Given
        Mockito.when(screeningRepository.findById(Mockito.any(ScreeningID.class))).thenReturn(Optional.empty());

        //When
        underTest.delete("Terminator", "Alpha", "2021.11.01 12:00");

        //Then
        Mockito.verify(console).printError("The start time has to match the required format! ( YYYY-MM-DD hh:mm )");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testConvertToScreeningDtoShouldReturnScreeningDtoWhenScreeningIsInput() {
        //Given

        //When
        ScreeningDto actual = underTest.convertToScreeningDto(screening);

        //Then
        Assertions.assertEquals(screeningDto, actual);
    }

    @Test
    void testConvertToScreeningDtoShouldReturnScreeningDtoWhenInputIsValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        ScreeningDto actual = underTest.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Assertions.assertEquals(screeningDto, actual);
    }

    @Test
    void testConvertToScreeningDtoShouldCallMovieRepository() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(movieRepository).findById("Terminator");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testConvertToScreeningDtoShouldCallRoomRepository() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        underTest.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00");

        //Then
        Mockito.verify(roomRepository).findById("Alpha");
        Mockito.verifyNoMoreInteractions(roomRepository);
    }

    @Test
    void testConvertToScreeningDtoShouldThrowNoSuchElementExceptionWhenMovieDoesntExists() {
        //Given

        //When
        Assertions.assertThrows(NoSuchElementException.class, () ->
                underTest.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"));

        //Then
    }

    @Test
    void testConvertToScreeningDtoShouldThrowNoSuchElementExceptionWhenRoomDoesntExists() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));

        //When
        Assertions.assertThrows(NoSuchElementException.class, () ->
                underTest.convertToScreeningDto("Terminator", "Alpha", "2021-11-01 12:00"));

        //Then
    }

    @Test
    void testConvertToScreeningDtoShouldThrowIllegalArgumentExceptionWhenTimeNotValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));
        Mockito.when(roomRepository.findById(Mockito.anyString())).thenReturn(Optional.of(alpha));

        //When
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                underTest.convertToScreeningDto("Terminator", "Alpha", "2021.11.01 12:00"));

        //Then
    }
}