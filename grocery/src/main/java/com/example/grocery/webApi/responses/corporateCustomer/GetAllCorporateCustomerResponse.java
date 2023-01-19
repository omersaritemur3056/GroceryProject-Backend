package com.example.grocery.webApi.responses.corporateCustomer;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllCorporateCustomerResponse {

    private Long id;

    private String username;

    private String email;

    private String password;

    private LocalDateTime createdDateTime;

    private LocalDateTime updatedDateTime;

    private boolean isActive;

    private String address;

    private String phoneNumber;

    private String companyName;

    private String taxNumber;
}
