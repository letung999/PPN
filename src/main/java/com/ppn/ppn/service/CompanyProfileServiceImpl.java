package com.ppn.ppn.service;

import com.ppn.ppn.dto.CompanyProfileDto;
import com.ppn.ppn.entities.CompanyProfile;
import com.ppn.ppn.exception.ResourcesNotFoundException;
import com.ppn.ppn.repository.CompanyRepository;
import com.ppn.ppn.service.constract.ICompanyProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CompanyProfileServiceImpl implements ICompanyProfileService {
    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<CompanyProfileDto> getInformationCompanyProfileByEstablishDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<CompanyProfile> companyProfiles = new ArrayList<>();
        try {
            Date dateOfEstablish = simpleDateFormat.parse(date);
            companyProfiles = companyRepository.getListCompanyProfile(dateOfEstablish);
        } catch (ParseException e) {
            log.error("cannot parse string input value date to date {}, with error: {}", date, e.getMessage());
        }

        if (companyProfiles.size() == 0) {
            throw new ResourcesNotFoundException("companyProfiles", date);
        }
        List<CompanyProfileDto> companyProfileDtos = companyProfiles.stream().map(companyProfile -> {
            CompanyProfileDto companyProfileDto = new CompanyProfileDto();
            companyProfileDto.setCompanyId(companyProfile.getCompanyId());
            companyProfileDto.setCompanyIdName(companyProfile.getCompanyIdName());
            companyProfileDto.setCompanyProfileMapping(companyProfile.getCompanyProfileMapping());
            return companyProfileDto;
        }).collect(Collectors.toList());

        return companyProfileDtos;
    }

    @Override
    public List<String> getAllListCompanyId(Pageable pageable) {
        List<String> companyIdList = new ArrayList<>();

        Page<CompanyProfile> profilePage = companyRepository.getListCompanyProfileId(pageable);
        if (profilePage.getContent().size() == 0) {
            throw new ResourcesNotFoundException();
        }
        companyIdList = profilePage.getContent()
                .stream()
                .map(companyProfile -> {
                    String companyId = companyProfile.getCompanyIdName();
                    return companyId;
                }).collect(Collectors.toList());
        return companyIdList;
    }
}
