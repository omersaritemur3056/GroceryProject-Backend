package com.example.grocery.api.responses.product;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdProductResponse {

    private Long id;
    private String name;
    private double price;
    private Long categoryId;
    private Long supplierId;
    private Long producerId;
    private List<Long> imageIds;
}
