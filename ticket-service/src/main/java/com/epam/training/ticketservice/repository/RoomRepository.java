package com.epam.training.ticketservice.repository;

import com.epam.training.ticketservice.model.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends CrudRepository<Room, String> {
    List<Room> findAll();
}
