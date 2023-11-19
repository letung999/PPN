package com.ppn.ppn.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "CompanyProfile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyProfile extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;

    @Column(name = "company_id_name")
    private String companyIdName;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dataOfEstablishment")
    private Date dateOfEstablishment;

    @OneToOne(mappedBy = "companyProfile", cascade = CascadeType.ALL)
    private CompanyProfileMapping companyProfileMapping;
}
