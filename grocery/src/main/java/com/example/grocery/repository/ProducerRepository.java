package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    boolean existsByNameIgnoreCase(String name);
}
