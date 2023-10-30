package com.ppn.ppn.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Car")
@Getter
@Setter
public class Car extends BaseEntity {
    @Id
    @Column(name = "carId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer carId;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @ManyToOne()
    @JoinColumn(name = "userId")
    private Users users;

    @OneToMany(mappedBy = "car")
    private List<CarInformation> carInformations;

    @ManyToMany()
    @JoinTable(
            joinColumns = @JoinColumn(name = "carId", referencedColumnName = "carId"),
            inverseJoinColumns = @JoinColumn(name = "workerId", referencedColumnName = "workerId", table = "Car"))
    List<Worker> workers;
}
