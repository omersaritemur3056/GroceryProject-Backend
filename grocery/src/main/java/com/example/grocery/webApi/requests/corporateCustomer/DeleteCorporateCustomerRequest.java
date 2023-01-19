package com.example.grocery.webApi.requests.corporateCustomer;

import jakarta.validation.constraints.Positive;

// import javax.validation.constraints.NotBlank;
// import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteCorporateCustomerRequest {

    @Positive
    private Long id;
}
