package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.dto.request.LoanStatementRequestDto;
import com.creditscoring.deal.entity.Passport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PassportMapper {

    @Mapping(target = "series", source = "passportSeries")
    @Mapping(target = "number", source = "passportNumber")
    Passport toPassportEntity(LoanStatementRequestDto dto);

    @Mapping(target = "issueDate", source = "passportIssueDate")
    @Mapping(target = "issueBranch", source = "passportIssueBranch")
    void updatePassportEntity(
            FinishRegistrationRequestDto dto,
            @MappingTarget Passport passport
    );
}