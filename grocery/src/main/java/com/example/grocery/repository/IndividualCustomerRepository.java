package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.IndividualCustomer;

public interface IndividualCustomerRepository extends JpaRepository<IndividualCustomer, Long> {

    boolean existsByNationalIdentity(String nationIdentity);

    boolean existsByUser_Id(Long id);

    boolean existsByImage_Id(Long id);
}
