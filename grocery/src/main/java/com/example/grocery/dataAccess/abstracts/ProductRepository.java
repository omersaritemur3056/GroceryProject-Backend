package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    boolean existsById(int id);

    boolean existsByNameIgnoreCase(String name);
}
