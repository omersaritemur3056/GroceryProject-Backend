package com.example.grocery.webApi.requests.product;

import java.time.LocalDate;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

    @NotBlank
    @NotNull
    @Size(min = 2, message = "name can not be lower than 2 character")
    private String name;

    @PositiveOrZero
    private double price;

    private String description;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future
    private LocalDate expirationDate;

    // validasyon kuralları testten sonra eklenecek
    @PositiveOrZero
    private int stock;

    @Positive
    private int categoryId;
}
