package com.ppn.ppn.dto;

import com.ppn.ppn.entities.CarInformation;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.entities.Worker;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CarDto extends BaseEntityDto{
    private Integer carId;
    private String make;
    private String model;
    private Users users;
    private List<CarInformation> carInformations;
    List<Worker> workers;
}
