package com.example.grocery.api.requests.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequest {

    @NotBlank
    @NotNull
    @Size(min = 2, message = "name can not be lower than 2 character")
    private String name;

    @PositiveOrZero
    private double price;

    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate productionDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate expirationDate;

    @PositiveOrZero
    private int stock;

    @Positive
    private Long categoryId;

    @Positive
    private Long supplierId;

    @Positive
    private Long producerId;

    private String[] imageUrls;
}
