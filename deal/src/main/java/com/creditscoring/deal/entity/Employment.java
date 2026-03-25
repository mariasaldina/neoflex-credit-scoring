package com.creditscoring.deal.entity;

import com.creditscoring.deal.enums.EmploymentStatus;
import com.creditscoring.deal.enums.EmploymentPosition;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employmentId;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    private String employerInn;
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;

    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
