package com.epam.training.ticketservice.core.screening.persistence.entity;

import com.epam.training.ticketservice.core.movie.persistence.entity.Movie;
import com.epam.training.ticketservice.core.room.persistence.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;


@Data
@Entity
@Table(name = "screenings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Screening {

    @EmbeddedId
    private ScreeningID id;

    @ManyToOne
    @MapsId("movieTitle")
    private Movie movie;

    @ManyToOne
    @MapsId("roomName")
    private Room room;
}
