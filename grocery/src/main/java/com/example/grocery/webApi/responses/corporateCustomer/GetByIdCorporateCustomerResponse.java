package com.example.grocery.webApi.responses.corporateCustomer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdCorporateCustomerResponse {

    private Long id;

    private Long userId;

    private String address;

    private String phoneNumber;

    private String companyName;

    private String taxNumber;
}
