package com.example.grocery.entity.concretes;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.example.grocery.entity.enums.OrderStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "delivered_date")
    private LocalDateTime deliveredDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_fk_id")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "customer_fk_id")
    private Customer customer;

    @ManyToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "orders_products", joinColumns = { @JoinColumn(name = "orders_order_id") }, inverseJoinColumns = {
            @JoinColumn(name = "products_product_id") })
    private List<Product> products; // set ile yapılabilir...
}
