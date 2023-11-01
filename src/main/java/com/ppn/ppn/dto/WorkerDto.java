package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Car;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class WorkerDto extends BaseEntityDto {
    private Integer workerId;
    private String email;
    private String status;
    private String firstName;
    private String lastName;
    private String gender;
    private BigDecimal price;
    private Integer experiences;
    private List<Car> cars;
}
