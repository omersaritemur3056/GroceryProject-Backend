package com.example.grocery.api.responses.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllCustomerResponse {

    private Long id;

    private Long userId;

    private Long imageId;

    private String address;

    private String phoneNumber;
}
