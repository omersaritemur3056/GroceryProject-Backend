package com.example.grocery.webApi.requests.category;

import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCategoryRequest {

    @Positive
    private Long id;
}
