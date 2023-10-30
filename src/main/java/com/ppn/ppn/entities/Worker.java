package com.ppn.ppn.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Worker")
@Getter
@Setter
public class Worker extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workerId")
    private Integer workerId;

    @Column(name = "email")
    private String email;

    @Column(name = "status")
    private String status;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "lastName")
    private String lastName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "experiences")
    private Integer experiences;

    @ManyToMany(mappedBy = "workers")
    private List<Car> cars;

}
