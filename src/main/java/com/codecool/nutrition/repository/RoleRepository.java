package com.codecool.nutrition.repository;

import com.codecool.nutrition.model.ERole;
import com.codecool.nutrition.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity,Long>{
    Optional<RoleEntity> findByName(ERole name);
}
