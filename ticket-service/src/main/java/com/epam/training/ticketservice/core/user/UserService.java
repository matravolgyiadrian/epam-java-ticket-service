package com.epam.training.ticketservice.core.user;

import com.epam.training.ticketservice.core.user.persistence.entity.User;

import java.util.List;

public interface UserService {
    boolean isSignedIn();

    boolean isAdmin();

    void signUp(String username, String password);

    void signIn(String username, String password, boolean isAdmin);

    void signOut();

    void describe();

    String getLoggedInUser();
}
