package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsById(Long id);

    boolean existsByNameIgnoreCase(String name);
}
