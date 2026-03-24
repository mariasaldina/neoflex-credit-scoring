package com.creditscoring.deal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Passport {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID passportId;

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
