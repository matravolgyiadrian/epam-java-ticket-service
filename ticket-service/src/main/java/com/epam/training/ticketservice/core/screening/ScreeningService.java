package com.epam.training.ticketservice.core.screening;

import com.epam.training.ticketservice.core.booking.persistence.entity.Seat;
import com.epam.training.ticketservice.core.screening.model.ScreeningDto;
import com.epam.training.ticketservice.core.screening.persistence.entity.Screening;
import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;

import javax.management.InstanceAlreadyExistsException;
import java.util.NoSuchElementException;
import java.util.Set;

public interface ScreeningService {
    void listAll();

    void create(String movieTitle, String roomName, String start);

    void delete(String movieTitle, String roomName, String start);

    ScreeningDto convertToScreeningDto(Screening screening);

    ScreeningDto convertToScreeningDto(String movieTitle, String roomName, String start) throws NoSuchElementException;
}
