package com.ppn.ppn.mapper;

import com.ppn.ppn.dto.CarInformationDto;
import com.ppn.ppn.entities.CarInformation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarInformationMapper {
    CarInformationMapper INSTANCE = Mappers.getMapper(CarInformationMapper.class);

    CarInformation carInformationDtoToCarInformation(CarInformationDto carInformationDto);

    CarInformationDto carInformationToCarInformationDto(CarInformation carInformation);
}
