package com.example.grocery.webApi.responses.product;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllProductResponse {

    private Long id;
    private String name;
    private double price;
    private String description;
    private LocalDate expirationDate;
    private int stock;
    private Long categoryId;
    private Long supplierId;
    private Long producerId;
}
