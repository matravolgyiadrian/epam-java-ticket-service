package com.epam.training.ticketservice.core.movie.persistence.repository;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, String> {
    List<Movie> findAll();
}
