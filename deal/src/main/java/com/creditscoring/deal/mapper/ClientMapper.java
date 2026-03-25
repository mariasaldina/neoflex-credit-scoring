package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmploymentMapper.class})
public interface ClientMapper {

    Client toEntity(LoanStatementRequestDto dto);
    void updateEntity(FinishRegistrationRequestDto finishDto, @MappingTarget Client client);
}
