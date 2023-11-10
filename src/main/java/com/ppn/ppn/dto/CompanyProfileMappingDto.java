package com.ppn.ppn.dto;

import com.ppn.ppn.entities.CompanyProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyProfileMappingDto extends BaseEntityDto {
    private Integer companyProfileMappingId;
    private Integer companyId;
    private String ParentCompanyId;
    private CompanyProfile companyProfile;
}
