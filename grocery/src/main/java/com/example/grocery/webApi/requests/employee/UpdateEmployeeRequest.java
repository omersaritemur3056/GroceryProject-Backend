package com.example.grocery.webApi.requests.employee;

import com.example.grocery.entity.enums.Nationality;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateEmployeeRequest {

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    @NotBlank
    @NotNull
    @Size(min = 11, max = 11)
    private String nationalIdentity;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate yearOfBirth;

    private Nationality nationality = Nationality.OTHER;

    @PositiveOrZero
    private double salary;

    private Long userId;

    private Long imageId;
}
