package com.example.grocery.core.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsById(int id);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
