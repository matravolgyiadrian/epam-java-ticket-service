package com.epam.training.ticketservice.core.user.impl;

import com.epam.training.ticketservice.core.booking.persistence.repository.BookingRepository;
import com.epam.training.ticketservice.core.user.UserService;
import com.epam.training.ticketservice.core.user.persistence.entity.User;
import com.epam.training.ticketservice.core.user.persistence.repository.UserRepository;
import com.epam.training.ticketservice.core.util.ConsoleService;
import com.epam.training.ticketservice.core.user.persistence.entity.AccountType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ConsoleService console;
    private final BookingRepository bookingRepository;

    private final AtomicBoolean signedIn = new AtomicBoolean();
    private final AtomicBoolean isAdmin = new AtomicBoolean();

    private String loggedInUser;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ConsoleService console, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.console = console;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public boolean isSignedIn() {
        return this.signedIn.get();
    }

    @Override
    public boolean isAdmin() {
        return this.isAdmin.get();
    }

    @Override
    public void signUp(String username, String password) {
        try {
            checkUsername(username);
            userRepository.save(User.builder()
                    .username(username)
                    .password(password)
                    .type(AccountType.USER)
                    .build());

        } catch (InstanceAlreadyExistsException e) {
            console.printError(e.getMessage());
        }

    }

    @Override
    public void signIn(String username, String password, boolean isAdmin) {
        List<User> users;
        if (isAdmin) {
            users = findAllAdminUser();
        } else {
            users = findAllNonAdminUser();
        }

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                this.signedIn.set(true);
                this.loggedInUser = user.getUsername();
                this.isAdmin.set(isAdmin);

                log.debug("Signed in with user '{}' ({})", user.getUsername(), user.getType().toString());
                break;
            }
        }
        if (!this.signedIn.get()) {
            console.printError("Login failed due to incorrect credentials");
        }
    }

    @Override
    public void signOut() {
        this.signedIn.set(false);

        log.debug("Signed out");
    }

    @Override
    public void describe() {
        if (signedIn.get()) {

            if (isAdmin.get()) {
                console.print("Signed in with privileged account '%s'", this.loggedInUser);
                console.print("%s", listAllByLoggedInUser());
            } else {
                console.print("Signed in with account '%s'", this.loggedInUser);
                console.print("%s", listAllByLoggedInUser());
            }
        } else {
            console.printError("Your are not signed in");
        }
    }

    @Override
    public String getLoggedInUser() {
        return loggedInUser;
    }

    private List<User> findAllAdminUser() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getType().equals(AccountType.ADMIN))
                .collect(Collectors.toList());
    }

    private List<User> findAllNonAdminUser() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getType().equals(AccountType.USER))
                .collect(Collectors.toList());
    }

    private void checkUsername(String username) throws InstanceAlreadyExistsException {
        boolean isTaken = userRepository.findAll()
                .stream()
                .anyMatch(user -> user.getUsername().equals(username));
        if (isTaken) {
            throw new InstanceAlreadyExistsException("This username is already taken.");
        }
    }

    private String listAllByLoggedInUser() {
        if (bookingRepository.findByUsername(loggedInUser).isEmpty()) {
            return "You have not booked any tickets yet";
        } else {
            StringBuilder sb = new StringBuilder("Your previous booking are");
            bookingRepository.findByUsername(loggedInUser)
                    .forEach(booking -> sb.append("\n").append(booking.toString()));
            return sb.toString();
        }
    }
}
