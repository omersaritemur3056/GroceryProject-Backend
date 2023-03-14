package com.example.grocery.api.requests.payment;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeletePaymentRequest {

    @Min(value = 1)
    private Long id;
}
