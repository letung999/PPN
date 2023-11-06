package com.ppn.ppn.controller;


import com.ppn.ppn.dto.RoleDto;
import com.ppn.ppn.payload.APIResponse;
import com.ppn.ppn.service.RoleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

import static com.ppn.ppn.constant.MessageStatus.INF_MSG_SUCCESSFULLY;

@RestController
@RequestMapping("api/v1/role")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid RoleDto roleDto) {
        RoleDto resultData = roleService.create(roleDto);
        APIResponse apiResponse = APIResponse.builder()
                .message(INF_MSG_SUCCESSFULLY)
                .timeStamp(LocalDateTime.now())
                .isSuccess(true)
                .statusCode(201)
                .data(resultData)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
