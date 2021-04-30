package com.epam.training.ticketservice.core.user.persistence.repository;

import com.epam.training.ticketservice.core.user.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
}
