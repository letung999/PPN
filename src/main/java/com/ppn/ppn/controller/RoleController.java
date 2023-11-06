package com.ppn.ppn.controller;


import com.ppn.ppn.dto.RoleDto;
import com.ppn.ppn.service.RoleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/role")
public class RoleController {
    @Autowired
    private RoleServiceImpl roleService;

    @PostMapping("/create")
    public ResponseEntity<RoleDto> create(@RequestBody @Valid RoleDto roleDto) {
        RoleDto resultData = roleService.create(roleDto);
        return ResponseEntity.ok(resultData);
    }
}
