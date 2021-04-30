package com.epam.training.ticketservice.core.room.impl;

import com.epam.training.ticketservice.core.room.RoomService;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import com.epam.training.ticketservice.core.room.persistence.repository.RoomRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final ConsoleService console;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, ConsoleService console) {
        this.roomRepository = roomRepository;
        this.console = console;
    }

    @Override
    public void listAll() {
        if (roomRepository.findAll().isEmpty()) {
            console.printError("There are no rooms at the moment");
        } else {
            roomRepository.findAll()
                    .stream()
                    .map(Room::toString)
                    .forEach(console::print);
        }
    }

    @Override
    public void create(String name, int rows, int columns) {
        if (roomRepository.findById(name).isPresent()) {
            console.printError("There is already a room named like this.");
        } else {
            roomRepository.save(
                    Room.builder()
                            .name(name)
                            .rows(rows)
                            .columns(columns)
                            .build());

            log.debug("Room created, with name: {}, rows: {}, columns: {}", name, rows, columns);
        }
    }

    @Override
    public void update(String name, int rows, int columns) {
        if (roomRepository.findById(name).isEmpty()) {
            console.printError("The room doesn't exists.");
        } else {
            roomRepository.save(
                    Room.builder()
                            .name(name)
                            .rows(rows)
                            .columns(columns)
                            .build());

            log.debug("Room updated, with name: {}, rows: {}, columns: {}", name, rows, columns);
        }
    }

    @Override
    public void delete(String name) {
        if (roomRepository.findById(name).isPresent()) {
            roomRepository.deleteById(name);

            log.debug("Room deleted, with name: {}", name);
        } else {
            console.printError("The room doesn't exists.");
        }
    }


    @Override
    public Collection<Room> findByName(String name) {
        return this.roomRepository.findAll()
                .stream()
                .filter(room -> room.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
