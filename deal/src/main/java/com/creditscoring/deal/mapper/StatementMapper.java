package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.response.StatementDto;
import com.creditscoring.deal.entity.Statement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClientMapper.class, CreditMapper.class})
public interface StatementMapper {

    StatementDto toStatementDto(Statement statement);
}