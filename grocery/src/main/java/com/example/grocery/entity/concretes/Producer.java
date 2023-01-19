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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "producers")
public class Producer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producer_id")
    private Long id;

    @Column(name = "producer_name")
    private String name;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;
}
