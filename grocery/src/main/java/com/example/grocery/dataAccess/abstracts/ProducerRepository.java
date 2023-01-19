package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Producer;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    boolean existsByNameIgnoreCase(String name);
}
