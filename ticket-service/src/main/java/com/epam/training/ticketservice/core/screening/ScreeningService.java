package com.epam.training.ticketservice.core.screening;

public interface ScreeningService {
    void listAll();

    void create(String movieTitle, String roomName, String start);

    void delete(String movieTitle, String roomName, String start);
}
