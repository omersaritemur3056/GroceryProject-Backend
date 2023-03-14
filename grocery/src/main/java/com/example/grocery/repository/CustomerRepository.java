package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
