package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.util.ScreeningCompositeKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "screenings")
@IdClass(ScreeningCompositeKey.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Screening {

    @Id
    private String movieTitle;

    @Id
    private String roomName;

    @Id
    private LocalDateTime startTime;

}
