package com.ppn.ppn.dto;

import com.ppn.ppn.entities.CompanyProfileMapping;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CompanyProfileDto extends BaseEntityDto {
    private Integer companyId;
    private String companyIdName;
    private Date dateOfEstablishment;
    private CompanyProfileMapping companyProfileMapping;
}
