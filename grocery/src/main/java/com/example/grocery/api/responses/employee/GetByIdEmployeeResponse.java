package com.example.grocery.api.responses.employee;

import java.time.LocalDate;

import com.example.grocery.model.enums.Gender;
import com.example.grocery.model.enums.Nationality;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByIdEmployeeResponse {

    private Long id;

    private Long userId;

    private Long imageId;

    private String firstName;

    private String lastName;

    private Gender gender;

    private String nationalIdentity;

    private Nationality nationality;

    private LocalDate birthYear;

    private double salary;
}
