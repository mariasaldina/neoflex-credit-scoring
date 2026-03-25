package com.creditscoring.deal.mapper;

import com.creditscoring.deal.dto.request.EmploymentDto;
import com.creditscoring.deal.entity.Employment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {

    Employment toEntity(EmploymentDto dto);
}
