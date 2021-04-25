package com.epam.training.ticketservice.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreeningCompositeKey implements Serializable {
    private String movieTitle;
    private String roomName;
    private LocalDateTime startTime;
}
