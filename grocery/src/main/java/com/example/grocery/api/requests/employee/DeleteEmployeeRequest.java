package com.example.grocery.api.requests.employee;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteEmployeeRequest {

    @Min(value = 1)
    private Long id;
}
