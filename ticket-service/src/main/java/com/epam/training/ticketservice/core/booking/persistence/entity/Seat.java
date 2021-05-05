package com.epam.training.ticketservice.core.booking.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private int seatRow;
    private int seatColumn;

    @Override
    public String toString() {
        return String.format("(%d,%d)", seatRow, seatColumn);
    }
}
