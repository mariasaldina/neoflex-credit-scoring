package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.request.EmploymentDto;
import com.creditscoring.deal.dto.response.EmploymentResponseDto;
import com.creditscoring.deal.entity.Employment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {

    @Mapping(target = "employerInn", source = "employerINN")
    @Mapping(target = "status", source = "employmentStatus")
    Employment toEmploymentEntity(EmploymentDto dto);

    EmploymentResponseDto toEmploymentResponseDto(Employment employment);
}
