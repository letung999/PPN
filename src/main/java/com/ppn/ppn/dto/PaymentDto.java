package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Users;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class PaymentDto extends BaseEntityDto{
    private Integer paymentId;
    private BigDecimal amount;
    private LocalDate dateOfPayment;
    private LocalTime timeOfPayment;
    private String paymentMethod;
    private Users users;
}
