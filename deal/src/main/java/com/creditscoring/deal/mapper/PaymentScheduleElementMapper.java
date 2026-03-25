package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.PaymentScheduleElementDto;
import com.creditscoring.deal.json.PaymentScheduleElement;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentScheduleElementMapper {

    PaymentScheduleElement toEntityField(PaymentScheduleElementDto dto);
}
