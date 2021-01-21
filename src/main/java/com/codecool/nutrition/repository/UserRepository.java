package com.codecool.nutrition.repository;

import com.codecool.nutrition.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByEmail(String email);
    UserEntity findByName(String name);

    UserEntity findByPlannerUsername(String name);

    UserEntity getUserByPlannerUsername(String name);

    Optional<UserEntity> getUserById(Long id);

    Boolean existsByName(String name);

    Boolean existsByEmail(String email);

}
