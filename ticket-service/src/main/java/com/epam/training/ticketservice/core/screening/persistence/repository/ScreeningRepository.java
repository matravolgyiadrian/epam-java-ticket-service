package com.epam.training.ticketservice.core.screening.persistence.repository;

import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningCompositeKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScreeningRepository extends CrudRepository<Screening, ScreeningCompositeKey> {
    List<Screening> findAll();
}
