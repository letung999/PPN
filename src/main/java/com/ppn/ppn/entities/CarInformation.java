package com.ppn.ppn.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "CarInformation")
@Getter
@Setter
public class CarInformation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carInformationId")
    private Integer carInformationId;

    @Column(name = "color")
    private String color;

    @Column(name = "maxSpeech")
    private Long maxSpeech;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "type")
    private String type;

    @Column(name = "year")
    private Integer year;

    @ManyToOne()
    @JoinColumn(name = "carId")
    private Car car;
}
