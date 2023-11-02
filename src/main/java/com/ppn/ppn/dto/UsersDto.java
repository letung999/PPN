package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Car;
import com.ppn.ppn.entities.Payment;
import com.ppn.ppn.entities.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UsersDto extends BaseEntityDto {
    private Integer userId;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "gender is required")
    private String gender;

    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;

    @NotBlank(message = "status is required")
    private String status;

    private List<Payment> payments;

    private List<Car> cars;

    Set<Role> roles;
}
