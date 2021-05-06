package com.epam.training.ticketservice.core.screening.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningID implements Serializable {
    private String movieTitle;
    private String roomName;
    private LocalDateTime startTime;
}
