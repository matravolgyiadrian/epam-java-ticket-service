package com.epam.training.ticketservice.core.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    String username;
    String password;

    @Enumerated(EnumType.STRING)
    AccountType type;
}
