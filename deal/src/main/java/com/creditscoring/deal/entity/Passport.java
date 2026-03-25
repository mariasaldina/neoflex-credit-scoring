package com.creditscoring.deal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Passport {

    public Passport(
            String series,
            String number
    ) {
        this.series = series;
        this.number = number;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID passportId;

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
}
