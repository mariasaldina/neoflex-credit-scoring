package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.Gender;
import com.creditscoring.deal.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Client {

    public Client(
            String firstName,
            String lastName,
            String middleName,
            LocalDate birthdate,
            String email
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthdate = birthdate;
        this.email = email;
    }

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
