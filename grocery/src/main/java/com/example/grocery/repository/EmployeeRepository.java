package com.example.grocery.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.grocery.model.concretes.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByNationalIdentity(String nationIdentity);

    boolean existsByUser_Id(Long id);

    boolean existsByImage_Id(Long id);
}
