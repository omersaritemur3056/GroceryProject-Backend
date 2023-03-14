package com.example.grocery.api.responses.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdSupplierResponse {

    private Long id;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;
}
