package com.example.grocery.webApi.requests.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.NotNull;
// import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryRequest {

    @NotNull
    @NotBlank
    @Size(min = 2, message = "name can not be lower than 2 character")
    private String name;
}
