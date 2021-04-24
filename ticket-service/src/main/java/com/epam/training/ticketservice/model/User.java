package com.epam.training.ticketservice.model;

import com.epam.training.ticketservice.util.AccountType;
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
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    String username;

    @Column(nullable = false)
    String password;

    @Column(nullable = false)
    AccountType type;
}
