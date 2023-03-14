package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
