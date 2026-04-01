package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.request.LoanOfferDto;
import com.creditscoring.deal.json.AppliedOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface OfferMapper {

    AppliedOffer toAppliedOfferJson(LoanOfferDto dto);

    @Mapping(target = "statementId", source = "statementId")
    LoanOfferDto updateStatementId(LoanOfferDto source, UUID statementId);
}
