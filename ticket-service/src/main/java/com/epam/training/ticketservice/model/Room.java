package com.epam.training.ticketservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "rooms")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    String name;

    @Column(nullable = false)
    int rows;

    @Column(nullable = false)
    int columns;

    public int getNumberOfSeats() {
        return rows * columns;
    }

    public String toString() {
        return String.format("Room %s with %d seats, %d rows and %d columns",
                this.name, getNumberOfSeats(), this.rows, this.columns);
    }
}
