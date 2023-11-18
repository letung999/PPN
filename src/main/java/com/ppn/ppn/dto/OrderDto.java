package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Payment;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDto {

    private long orderId;

    private String orderTrackingNumber;

    private int totalQuantity;

    private BigDecimal totalPrice;

    private String status;

    private long shoppingCardId;

    private List<Payment> paymentList;
}
