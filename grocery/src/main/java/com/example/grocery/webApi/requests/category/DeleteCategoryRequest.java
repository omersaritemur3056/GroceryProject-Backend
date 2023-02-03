package com.example.grocery.webApi.requests.category;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCategoryRequest {

    @Min(value = 1)
    private Long id;
}
