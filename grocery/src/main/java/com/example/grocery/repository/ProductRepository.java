package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long id);

    boolean existsByNameIgnoreCase(String name);

    List<Product> findByExpirationDateBefore(LocalDate expirationDate);

    Optional<List<Product>> findAllByCategory_Id(Long categoryId);
}
