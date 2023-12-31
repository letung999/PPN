package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Car;
import com.ppn.ppn.entities.Payment;
import com.ppn.ppn.entities.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDto extends BaseEntityDto {
    private Integer userId;

    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "firstName is required")
    private String firstName;

    @NotBlank(message = "gender is required")
    private String gender;

    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;

    private String status;

    private String verifyCode;

    private List<Payment> payments;

    private List<Car> cars;

    Set<Role> roles;
}
