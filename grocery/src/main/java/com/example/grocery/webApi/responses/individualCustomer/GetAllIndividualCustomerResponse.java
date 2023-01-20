package com.example.grocery.webApi.responses.individualCustomer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllIndividualCustomerResponse {

    private Long id;

    private Long userId;

    private String address;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String nationalIdentity;
}
