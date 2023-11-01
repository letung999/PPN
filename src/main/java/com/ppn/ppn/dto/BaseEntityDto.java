package com.ppn.ppn.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BaseEntityDto {
    private LocalDate createdDate;
    private LocalDate updatedDate;
    private String createdBy;
    private String updatedBy;
}
