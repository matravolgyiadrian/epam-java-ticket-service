package com.epam.training.ticketservice.core.booking.persistence.repository;

import com.epam.training.ticketservice.core.booking.persistence.entity.Booking;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAll();

    List<Booking> findByUsername(String username);

    List<Booking> findByScreening(ScreeningID screening);
}
