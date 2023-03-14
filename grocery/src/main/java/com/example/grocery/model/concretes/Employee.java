package com.example.grocery.model.concretes;

import com.example.grocery.core.security.models.User;
import com.example.grocery.model.enums.Gender;
import com.example.grocery.model.enums.Nationality;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "national_identity", nullable = true, unique = true)
    private String nationalIdentity;

    @Column(name = "birth_year", nullable = false)
    private LocalDate birthYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false)
    private Nationality nationality;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Column(name = "salary")
    private double salary;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk_id", referencedColumnName = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_fk_id", unique = true)
    private Image image;

}
