package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {EmploymentMapper.class, PassportMapper.class})
public interface ClientMapper {

    @Mapping(target = "passport", source = "dto")
    Client toClientEntity(LoanStatementRequestDto dto);
    void updateClientEntity(FinishRegistrationRequestDto finishDto, @MappingTarget Client client);
}
