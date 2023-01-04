package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsById(int id);

    boolean existsByEmail(String email);

    User findByEmail(String email);
}
