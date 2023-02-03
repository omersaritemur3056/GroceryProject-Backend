package com.example.grocery.webApi.requests.producer;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteProducerRequest {

    @Min(value = 1)
    private Long id;
}
