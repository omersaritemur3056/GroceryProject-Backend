package com.example.grocery.webApi.responses.individualCustomer;

import java.time.LocalDateTime;

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

    private String username;

    private String email;

    private String password;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private boolean isActive;

    private String address;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String nationalIdentity;
}
