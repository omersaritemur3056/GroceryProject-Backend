package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.IndividualCustomer;

public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, Long> {

    boolean existsByNationalIdentity(String nationIdentity);

    boolean existsByUser_Id(Long id);

    boolean existsByImage_Id(Long id);
}
