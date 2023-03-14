package com.example.grocery.entity.concretes;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(name = "image_public_id")
    private String publicId;

    @Column(name = "image_url")
    private String url;

    @Column(name = "image_bytes")
    private int bytes;

    @Column(name = "image_format")
    private String format;

    @Column(name = "image_height")
    private int height;

    @Column(name = "image_width")
    private int width;

    @OneToMany(mappedBy = "image")
    private List<Customer> customers;

    @OneToMany(mappedBy = "image")
    private List<Employee> employees;

    @ManyToMany(mappedBy = "images")
    private List<Product> products;
}
