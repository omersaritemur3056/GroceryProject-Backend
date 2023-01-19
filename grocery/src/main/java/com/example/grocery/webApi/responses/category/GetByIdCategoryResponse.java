package com.example.grocery.webApi.responses.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdCategoryResponse {

    private Long id;
    private String name;
}
