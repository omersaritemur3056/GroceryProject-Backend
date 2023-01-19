package com.example.grocery.webApi.requests.producer;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeleteProducerRequest {

    @Positive
    private Long id;
}
