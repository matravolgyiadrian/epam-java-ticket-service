package com.epam.training.ticketservice.core.booking;


public interface BookingService {
    void create(String movieTitle, String roomName, String start, String seatsString);
}
