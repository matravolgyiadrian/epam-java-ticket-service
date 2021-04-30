package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.persistence.entity.User;

import java.util.List;

public interface UserService {
    boolean isSignedIn();

    void signIn(String username, String password);

    void signOut();

    void describe();
}
