package com.example.grocery.entity.concretes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @Column(name = "producer_name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;
}
