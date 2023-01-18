package com.example.grocery.core.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.core.security.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsById(int id);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
}
