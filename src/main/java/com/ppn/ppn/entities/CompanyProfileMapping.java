package com.ppn.ppn.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "company_profile_mapping")
@Getter
@Setter
public class CompanyProfileMapping extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyProfileMappingId;

    @Column(name = "company_id")
    private Integer companyId;

    @Column(name = "parent_company_id")
    private String ParentCompanyId;

    @JsonIgnore
    @OneToOne(mappedBy = "companyProfileMapping")
    private CompanyProfile companyProfile;
}
