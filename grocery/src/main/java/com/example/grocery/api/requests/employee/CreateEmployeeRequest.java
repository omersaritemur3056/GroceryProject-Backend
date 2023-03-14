package com.example.grocery.api.requests.employee;

import com.example.grocery.model.enums.Gender;
import com.example.grocery.model.enums.Nationality;
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
public class CreateEmployeeRequest {

    @NotBlank
    @NotNull
    private String firstName;

    @NotBlank
    @NotNull
    private String lastName;

    @NotNull
    private Gender gender;

    @Size(min = 11, max = 11)
    @Pattern(regexp = "^[1-9][0-9]{10}$")
    private String nationalIdentity;

    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthYear;

    @NotNull
    private Nationality nationality;

    @PositiveOrZero
    private double salary;

    @Min(value = 1)
    private Long userId;

    @Min(value = 1)
    private Long imageId;
}
