package com.ppn.ppn.repository;


import com.ppn.ppn.entities.CompanyProfile;
import com.ppn.ppn.entities.CompanyProfileMapping;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyRepositoryTests {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyProfileMappingRepository companyProfileMappingRepository;

    private SimpleDateFormat simpleDateFormat;
    private Date inputDate;
    private Date inputDate1;

    @BeforeEach
    public void setup() throws ParseException {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        inputDate = simpleDateFormat.parse("2009-10-01");
        inputDate1 = simpleDateFormat.parse("2004-10-01");

        CompanyProfile companyProfile = CompanyProfile.builder()
                .companyIdName("001_shopBack")
                .dateOfEstablishment(inputDate)
                .build();

        CompanyProfile companyProfile1 = CompanyProfile.builder()
                .companyIdName("002_ManHeim")
                .dateOfEstablishment(inputDate1)
                .build();

        CompanyProfile companyProfileSaved = companyRepository.save(companyProfile);
        CompanyProfile companyProfileSaved1 = companyRepository.save(companyProfile1);

        CompanyProfileMapping companyProfileMapping = CompanyProfileMapping.builder()
                .companyId(companyProfileSaved.getCompanyId())
                .parentCompanyId("001_shopBack")
                .companyProfile(companyProfileSaved)
                .build();
        CompanyProfileMapping companyProfileMapping1 = CompanyProfileMapping.builder()
                .companyId(companyProfileSaved1.getCompanyId())
                .parentCompanyId("002_ManHeim")
                .companyProfile(companyProfileSaved1)
                .build();

        companyProfileMappingRepository.save(companyProfileMapping);
        companyProfileMappingRepository.save(companyProfileMapping1);
    }

    @Test
    public void givenListCompanyProfile_whenGetListCompanyProfileByDate_thenListCompanyProfile() throws ParseException {

        //action
        List<CompanyProfile> resultData = companyRepository.getListCompanyProfile(inputDate1);

        //output
        Assertions.assertThat(resultData).isNotNull();
        Assertions.assertThat(resultData.size()).isEqualTo(2);
    }

    @Test
    public void givenListCompanyProfileId_whenGetListCompanyProfileId_thenListCompanyProfileId() throws ParseException {

        PageRequest pageRequest = PageRequest.of(0, 2);

        //action
        Page<CompanyProfile> companyProfilePage = companyRepository.getListCompanyProfileId(pageRequest);
        List<CompanyProfile> companyProfiles = companyProfilePage.getContent();

        //output
        Assertions.assertThat(companyProfiles).isNotNull();
        Assertions.assertThat(companyProfiles.size()).isEqualTo(2);
    }

    @Test
    public void givenListCompanyProfileIdIsEmpty_whenGetListCompanyProfileId_givenListCompanyProfileIdIsEmpty() throws ParseException {

        PageRequest pageRequest = PageRequest.of(1, 2);

        //action
        Page<CompanyProfile> companyProfilePage = companyRepository.getListCompanyProfileId(pageRequest);
        List<CompanyProfile> companyProfiles = companyProfilePage.getContent();

        //output
        Assertions.assertThat(companyProfiles).isEmpty();
    }
}
