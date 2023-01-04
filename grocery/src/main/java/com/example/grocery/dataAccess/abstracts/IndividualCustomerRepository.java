package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.IndividualCustomer;

public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, Integer> {

    boolean existsByNationalIdentity(String nationIdentity);
}
