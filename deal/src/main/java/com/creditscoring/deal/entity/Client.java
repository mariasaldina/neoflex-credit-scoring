package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.Gender;
import com.creditscoring.deal.enums.MaritalStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID clientId;

    private String lastName;
    private String firstName;
    private String middleName;
    private LocalDate birthdate;
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;
    private UUID passportId;
    private UUID employmentId;
    private String accountNumber;
}
