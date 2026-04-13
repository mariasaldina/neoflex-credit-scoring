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
    @Column(nullable = false)
    private EmploymentStatus status;

    @Column(nullable = false)
    private String employerInn;

    @Column(nullable = false)
    private BigDecimal salary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmploymentPosition position;

    @Column(nullable = false)
    private Integer workExperienceTotal;

    @Column(nullable = false)
    private Integer workExperienceCurrent;
}
