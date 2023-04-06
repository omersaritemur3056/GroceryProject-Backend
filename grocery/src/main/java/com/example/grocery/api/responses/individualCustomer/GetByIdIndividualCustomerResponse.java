package com.example.grocery.api.responses.individualCustomer;

import com.example.grocery.model.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdIndividualCustomerResponse {

    private Long id;

    private Long userId;

    private Long imageId;

    private String address;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String nationalIdentity;

    private String imageUrl;
}
