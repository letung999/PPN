package com.ppn.ppn.controller;

import com.ppn.ppn.dto.CompanyProfileDto;
import com.ppn.ppn.payload.APIResponse;
import com.ppn.ppn.service.CompanyProfileServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static com.ppn.ppn.constant.MessageStatus.INF_MSG_SUCCESSFULLY;

@RestController
@RequestMapping("/api/v1/company")
public class CompanyController {
    @Autowired
    private CompanyProfileServiceImpl companyProfileService;

    @GetMapping("/dateOfEstablishment")
    public ResponseEntity<?> getInformationCompanyByDateOfEstablish(@RequestParam String date) {
        List<CompanyProfileDto> companyProfiles = companyProfileService.getInformationCompanyProfileByEstablishDate(date);
        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .timeStamp(LocalDateTime.now())
                .statusCode(200)
                .isSuccess(true)
                .data(companyProfiles)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/all-companyId")
    public ResponseEntity<?> getAllCompanyId(@RequestParam @Valid Integer page,
                                             @RequestParam @Valid Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<String> companyIds = companyProfileService.getAllListCompanyId(pageRequest);
        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .timeStamp(LocalDateTime.now())
                .statusCode(200)
                .isSuccess(true)
                .data(companyIds)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
