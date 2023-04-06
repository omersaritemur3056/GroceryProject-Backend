package com.example.grocery.api.responses.corporateCustomer;

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

    private Long userId;

    private Long imageId;

    private String address;

    private String phoneNumber;

    private String companyName;

    private String taxNumber;

    private String imageUrl;
}
