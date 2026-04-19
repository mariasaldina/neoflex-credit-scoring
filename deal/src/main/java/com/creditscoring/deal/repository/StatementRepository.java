package com.creditscoring.deal.repository;

import com.creditscoring.deal.entity.Statement;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;
import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Statement> findByStatementId(UUID statementId);
}
