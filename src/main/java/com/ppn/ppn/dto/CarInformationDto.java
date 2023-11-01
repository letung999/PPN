package com.ppn.ppn.dto;


import com.ppn.ppn.entities.Car;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarInformationDto extends BaseEntityDto{
    private Integer carInformationId;
    private String color;
    private Long maxSpeech;
    private BigDecimal price;
    private String type;
    private Integer year;
    private Car car;
}
