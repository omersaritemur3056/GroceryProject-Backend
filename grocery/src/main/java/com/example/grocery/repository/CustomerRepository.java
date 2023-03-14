package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
