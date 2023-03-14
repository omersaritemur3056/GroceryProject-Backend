package com.example.grocery.webApi.responses.supplier;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllSupplierResponse {

    private Long id;

    private String name;

    private String address;

    private String phoneNumber;

    private String email;
}
