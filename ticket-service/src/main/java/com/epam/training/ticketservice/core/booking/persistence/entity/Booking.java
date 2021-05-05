package com.epam.training.ticketservice.core.booking.persistence.entity;

import com.epam.training.ticketservice.core.screening.persistence.entity.ScreeningID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GenerationType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Data
@Entity
@Table(name = "bookings")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String username;

    private ScreeningID screening;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "booking_seats", joinColumns = @JoinColumn(name = "seat_id"))
    private Set<Seat> seats;

    @Override
    public String toString() {
        return String.format("Seats%s on %s in room %s starting at %s for %d HUF",
                seats.toString().replace("[", "").replace("]", ""),
                screening.getMovieTitle(), screening.getRoomName(),
                screening.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), seats.size() * 1500);
    }

}
