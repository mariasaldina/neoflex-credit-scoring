package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.ScoringDataDto;
import com.creditscoring.deal.dto.request.FinishRegistrationRequestDto;
import com.creditscoring.deal.entity.Statement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScoringDataMapper {

    @Mapping(target = "amount", expression = "java(statement.getAppliedOffer().requestedAmount())")
    @Mapping(target = "term", expression = "java(statement.getAppliedOffer().term())")
    @Mapping(target = "firstName", expression = "java(statement.getClient().getFirstName())")
    @Mapping(target = "lastName", expression = "java(statement.getClient().getLastName())")
    @Mapping(target = "middleName", expression = "java(statement.getClient().getMiddleName())")
    @Mapping(target = "birthdate", expression = "java(statement.getClient().getBirthdate())")
    @Mapping(target = "passportSeries", expression = "java(statement.getClient().getPassport().getSeries())")
    @Mapping(target = "passportNumber", expression = "java(statement.getClient().getPassport().getNumber())")
    @Mapping(target = "isInsuranceEnabled",expression = "java(statement.getAppliedOffer().isInsuranceEnabled())")
    @Mapping(target = "isSalaryClient", expression = "java(statement.getAppliedOffer().isSalaryClient())")
    ScoringDataDto toDto(Statement statement, FinishRegistrationRequestDto finishDto);
}
