package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.entity.Credit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PaymentScheduleElementMapper.class})
public interface CreditMapper {

    @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "isSalaryClient")
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCreditEntity(CreditDto dto);

    CreditDto toCreditDto(Credit credit);
}
