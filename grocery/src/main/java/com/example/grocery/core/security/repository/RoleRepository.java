package com.example.grocery.core.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.core.security.enums.Authority;
import com.example.grocery.core.security.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(Authority name);
}
