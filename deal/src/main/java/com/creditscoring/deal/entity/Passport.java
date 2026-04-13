package com.creditscoring.deal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID passportId;

    @Column(nullable = false)
    private String series;

    @Column(nullable = false)
    private String number;

    private String issueBranch;
    private LocalDate issueDate;
}
