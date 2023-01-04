package com.example.grocery.entity.concretes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

// import javax.persistence.Column;
// import javax.persistence.Entity;
// import javax.persistence.Inheritance;
// import javax.persistence.InheritanceType;
// import javax.persistence.PrimaryKeyJoinColumn;
// import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "customers")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "customer_id", referencedColumnName = "user_id")
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends User {

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

}
