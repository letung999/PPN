package com.ppn.ppn.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "Payment")
@Entity
@Getter
@Setter
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer paymentId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "dateOfPayment")
    private LocalDate dateOfPayment;

    @Column(name = "timeOfPayment")
    private LocalTime timeOfPayment;

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @Column(name = "cardName")
    private String cardName;

    @Column(name = "cardNumber")
    private String cardNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "userId")
    private Users users;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;
}
