package com.example.grocery.dataAccess.abstracts;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsById(Long id);

    boolean existsByNameIgnoreCase(String name);

    Optional<Category> findByName(String name);
}
