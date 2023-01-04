package com.example.grocery.dataAccess.abstracts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    boolean existsById(int id);

    boolean existsByName(String name);

    Optional<Category> findByName(String name);
}
