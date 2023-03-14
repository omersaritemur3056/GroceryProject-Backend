package com.example.grocery.dataAccess.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.entity.concretes.CorporateCustomer;

public interface CorporateCustomerRepository extends JpaRepository<CorporateCustomer, Long> {

    boolean existsByTaxNumber(String taxNumber);

    boolean existsByUser_Id(Long id);

    boolean existsByImage_Id(Long id);
}
