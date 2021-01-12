package com.codecool.nutrition.repository;

import com.codecool.nutrition.model.ERole;
import com.codecool.nutrition.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByName(ERole name);
}
