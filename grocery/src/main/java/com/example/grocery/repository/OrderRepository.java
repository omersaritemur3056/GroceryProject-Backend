package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
