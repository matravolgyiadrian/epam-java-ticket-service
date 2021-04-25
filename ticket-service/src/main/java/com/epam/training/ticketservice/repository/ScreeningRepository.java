package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Screening;
import com.epam.training.ticketservice.util.ScreeningCompositeKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreeningRepository extends CrudRepository<Screening, ScreeningCompositeKey> {
    List<Screening> findAll();
}
