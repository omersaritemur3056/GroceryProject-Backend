package com.example.grocery.model.concretes;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
import lombok.ToString;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false)
    private String name;

    @Column(name = "product_price", nullable = false)
    private double price;

    @Column(name = "product_description")
    private String description;

    @Column(name = "product_production_date", nullable = false)
    private LocalDate productionDate;

    @Column(name = "product_expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "product_stock")
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "supplier_fk_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "producer_fk_id", nullable = false)
    private Producer producer;

    @ManyToMany(mappedBy = "products", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<Order> orders;

    @ManyToMany
    @JoinTable(name = "products_images", joinColumns = {
            @JoinColumn(name = "products_product_id") }, inverseJoinColumns = { @JoinColumn(name = "images_image_id") })
    private List<Image> images;

}
