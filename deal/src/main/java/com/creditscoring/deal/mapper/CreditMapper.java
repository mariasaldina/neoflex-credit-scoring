package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.calculator.response.CreditDto;
import com.creditscoring.deal.entity.Credit;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PaymentScheduleElementMapper.class})
public interface CreditMapper {

    Credit toEntity(CreditDto dto);
}
