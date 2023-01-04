package com.example.grocery.webApi.responses.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdProductResponse {

    private int id;
    private String name;
    private double price;
    private int categoryId;
}
