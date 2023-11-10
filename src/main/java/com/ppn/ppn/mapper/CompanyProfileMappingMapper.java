package com.ppn.ppn.mapper;


import com.ppn.ppn.dto.CompanyProfileMappingDto;
import com.ppn.ppn.entities.CompanyProfileMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CompanyProfileMappingMapper {
    CompanyProfileMappingMapper INSTANCE = Mappers.getMapper(CompanyProfileMappingMapper.class);

    CompanyProfileMappingDto companyProfileMappingToCompanyProfileMappingDto(CompanyProfileMapping companyProfileMapping);

    CompanyProfileMapping companyProfileMappingDtoToCompanyProfileMapping(CompanyProfileMappingDto companyProfileMappingDto);
}
