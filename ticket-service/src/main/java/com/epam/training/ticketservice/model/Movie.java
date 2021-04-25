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
@Table(name = "movies")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    String title;

    @Column(nullable = false)
    String genre;

    @Column(nullable = false)
    int length;

    public String toString() {
        return String.format("%s (%s, %d minutes)", this.title, this.genre, this.length);
    }
}
