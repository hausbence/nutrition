package com.codecool.nutrition.repository;

import com.codecool.nutrition.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    Optional<User> getUserById(Long id);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);

}
