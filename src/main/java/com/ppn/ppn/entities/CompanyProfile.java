package com.ppn.ppn.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "CompanyProfile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfile extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;

    @Column(name = "company_id_name")
    private String companyIdName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataOfEstablishment")
    private Date dateOfEstablishment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_profile_mapping_Id")
    private CompanyProfileMapping companyProfileMapping;
}
