package com.example.grocery.webApi.requests.product;

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

    @Min(value = 1)
    private Long categoryId;

    @Min(value = 1)
    private Long supplierId;

    @Min(value = 1)
    private Long producerId;

    private Long[] imageIds;
}
