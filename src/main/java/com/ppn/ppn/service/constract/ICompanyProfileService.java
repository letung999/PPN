package com.ppn.ppn.service.constract;

import com.ppn.ppn.dto.CompanyProfileDto;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface ICompanyProfileService {
    List<CompanyProfileDto> getInformationCompanyProfileByEstablishDate(String date);

    List<String> getAllListCompanyId(Pageable pageable);
}
