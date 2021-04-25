package com.epam.training.ticketservice.util;

import com.epam.training.ticketservice.model.User;
import com.epam.training.ticketservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitDB implements InitializingBean {
    private final UserRepository userRepository;

    @Autowired
    public InitDB(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        userRepository.save(User.builder()
                .username("admin")
                .password("admin")
                .type(AccountType.ADMIN)
                .build());
        log.info("Admin user added to the database with name: 'admin'");
    }
}
