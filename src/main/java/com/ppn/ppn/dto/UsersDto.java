package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Car;
import com.ppn.ppn.entities.Payment;
import com.ppn.ppn.entities.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UsersDto extends BaseEntityDto{
    private Integer userId;
    private String email;
    private String firstName;
    private String gender;
    private String phoneNumber;
    private String status;
    private List<Payment> payments;
    private List<Car> cars;
    Set<Role> roles;
}
