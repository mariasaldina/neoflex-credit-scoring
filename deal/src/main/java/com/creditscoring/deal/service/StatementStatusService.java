package com.creditscoring.deal.service;

import com.creditscoring.deal.entity.Statement;
import com.creditscoring.deal.enums.ApplicationStatus;
import com.creditscoring.deal.repository.StatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementStatusService {

    private final StatementRepository statementRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deny(UUID statementId) {
        Statement statement = statementRepository.findById(statementId).orElseThrow();
        statement.changeStatus(ApplicationStatus.CC_DENIED);
    }
}