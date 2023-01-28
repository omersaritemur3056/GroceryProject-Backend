package com.example.grocery.entity.concretes;

import com.example.grocery.core.security.models.User;
import com.example.grocery.entity.enums.Nationality;
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

    @Column(name = "year_of_birth", nullable = false)
    private LocalDate yearOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "nationality", nullable = false)
    private Nationality nationality;

    @Column(name = "salary")
    private double salary;

    @OneToOne
    @JoinColumn(name = "user_fk_id", referencedColumnName = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "image_fk_id")
    private Image image;

}
