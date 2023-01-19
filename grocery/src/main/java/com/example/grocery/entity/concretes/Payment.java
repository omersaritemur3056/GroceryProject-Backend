package com.example.grocery.entity.concretes;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "card_expiration_year")
    private int cardExpirationYear;

    @Column(name = "card_expiration_month")
    private int cardExpirationMonth;

    @Column(name = "card_cvv")
    private String cardCvv;

    @Column(name = "balance")
    private double balance;

    @OneToMany(mappedBy = "payment")
    private List<Order> orders;
}
