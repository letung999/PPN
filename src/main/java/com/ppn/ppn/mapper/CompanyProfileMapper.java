package com.ppn.ppn.mapper;

import com.ppn.ppn.dto.CompanyProfileDto;
import com.ppn.ppn.entities.CompanyProfile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyProfileMapper {
    CompanyProfileMapper INSTANCE = Mappers.getMapper(CompanyProfileMapper.class);

    CompanyProfileDto companyProfileToCompanyProfileDto(CompanyProfile companyProfile);

    CompanyProfile companyProfileDtoToCompanyProfile(CompanyProfileDto companyProfileDto);
}
