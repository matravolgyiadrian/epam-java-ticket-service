package com.epam.training.ticketservice.core.movie.persistence.repository;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, String> {
    List<Movie> findAll();
}
