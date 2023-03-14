package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
