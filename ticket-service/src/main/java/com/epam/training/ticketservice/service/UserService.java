package com.epam.training.ticketservice.service;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.repository.UserRepository;
import com.epam.training.ticketservice.util.AccountType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ConsoleService console;

    private final AtomicBoolean signedIn = new AtomicBoolean();

    private String loggedInUser;

    @Autowired
    public UserService(UserRepository userRepository, ConsoleService console) {
        this.userRepository = userRepository;
        this.console = console;
    }

    public List<User> findAllAdminUser() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getType().equals(AccountType.ADMIN))
                .collect(Collectors.toList());
    }

    public boolean isSignedIn() {
        return this.signedIn.get();
    }

    public void signIn(String username, String password) {
        List<User> admins = findAllAdminUser();

        for (User user : admins) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.signedIn.set(true);
                this.loggedInUser = user.getUsername();

                log.debug("Signed in with user '{}'", user.getUsername());
                break;
            }
        }
        if (!this.signedIn.get()) {
            console.printError("Login failed due to incorrect credentials");
        }
    }

    public void signOut() {
        this.signedIn.set(false);

        log.debug("Signed out");
    }

    public void describe() {
        if (signedIn.get()) {
            console.print("Signed in with privileged account '%s'", this.loggedInUser);
        } else {
            console.printError("Your are not signed in");
        }
    }
}
