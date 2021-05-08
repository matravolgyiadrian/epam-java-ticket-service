package com.epam.training.ticketservice.core.movie.impl;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.movie.persistence.repository.MovieRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;


class MovieServiceImplTest {

    private static final Movie terminator = Movie.builder()
            .title("Terminator")
            .genre("action")
            .length(120)
            .build();
    private static final Movie updatedTerminator = new Movie("Terminator", "sci-fi", 120);

    private MovieServiceImpl underTest;
    private MovieRepository movieRepository;
    private ConsoleService console;

    @BeforeEach
    public void init(){
        movieRepository = Mockito.mock(MovieRepository.class);
        console = Mockito.mock(ConsoleService.class);
        underTest = new MovieServiceImpl(movieRepository, console);
    }

    @Test
    void testListAllShouldCallMovieRepository() {
        //Given

        //When
        underTest.listAll();

        //Then
        Mockito.verify(movieRepository).findAll();
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testListAllShouldPrintMovies() {
        //Given
        Mockito.when(movieRepository.findAll()).thenReturn(List.of(terminator));

        //When
        underTest.listAll();

        //Then
        Mockito.verify(console).print("Terminator (action, 120 minutes)");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testListAllShouldCallConsoleAndPrintErrorWhenListIsEmpty() {
        //Given
        Mockito.when(movieRepository.findAll()).thenReturn(List.of());

        //When
        underTest.listAll();

        //Then
        Mockito.verify(console).printError("There are no movies at the moment");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testCreateShouldCallMovieRepositoryWhenTheInputIsValid() {
        //Given

        //When
        underTest.create("Terminator", "action", 120);

        //Then
        Mockito.verify(movieRepository).save(terminator);
        Mockito.verify(movieRepository).findById("Terminator");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testCreateShouldCallConsoleAndPrintErrorWhenTheInputIsAlreadyExists() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));

        //When
        underTest.create("Terminator", "action", 120);

        //Then
        Mockito.verify(console).printError("This movie is already in our database.");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testUpdateShouldCallMoveRepositoryWhenInputIsValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));

        //When
        underTest.update("Terminator", "sci-fi", 120);

        //Then
        Mockito.verify(movieRepository).save(updatedTerminator);
        Mockito.verify(movieRepository).findById("Terminator");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testUpdateShouldCallConsoleAndPrintErrorWhenMovieDoesntExists() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        //When
        underTest.update("Terminator", "sci-fi", 120);

        //Then
        Mockito.verify(console).printError("There is no movie with title: %s", "Terminator");
        Mockito.verifyNoMoreInteractions(console);
    }

    @Test
    void testDeleteShouldCallMovieRepositoryWhenInputIsValid() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.of(terminator));

        //When
        underTest.delete("Terminator");

        //Then
        Mockito.verify(movieRepository).deleteById("Terminator");
        Mockito.verify(movieRepository).findById("Terminator");
        Mockito.verifyNoMoreInteractions(movieRepository);
    }

    @Test
    void testDeleteShouldCallConsoleAndPrintErrorWhenMovieDoesntExists() {
        //Given
        Mockito.when(movieRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

        //When
        underTest.delete("Terminator");

        //Then
        Mockito.verify(console).printError("The movie doesn't exist.");
        Mockito.verifyNoMoreInteractions(console);
    }
}