package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.CorporateCustomer;

public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Integer> {

    boolean existsByTaxNumber(String taxNumber);
}
