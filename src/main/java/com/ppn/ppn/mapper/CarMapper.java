package com.ppn.ppn.mapper;

import com.ppn.ppn.dto.CarDto;
import com.ppn.ppn.entities.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    Car carDtoToCar(CarDto carDto);

    CarDto carToCarDto(Car car);
}
