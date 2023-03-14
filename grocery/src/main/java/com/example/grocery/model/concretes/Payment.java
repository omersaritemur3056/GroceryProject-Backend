package com.example.grocery.model.concretes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "card_expiration_year", nullable = false)
    private int cardExpirationYear;

    @Column(name = "card_expiration_month", nullable = false)
    private int cardExpirationMonth;

    @Column(name = "card_cvv", nullable = false)
    private String cardCvv;

    @Column(name = "balance")
    private double balance;

    @OneToMany(mappedBy = "payment")
    private List<Order> orders;
}
