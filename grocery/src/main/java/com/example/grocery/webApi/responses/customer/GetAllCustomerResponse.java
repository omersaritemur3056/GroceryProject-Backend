package com.example.grocery.webApi.responses.customer;

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

    private String address;

    private String phoneNumber;
}
