package com.ppn.ppn.controller;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.service.UsersServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/users")
@RestController
public class UserController {
    @Autowired
    private UsersServiceImpl usersService;

    @PostMapping("/create")
    public ResponseEntity<UsersDto> add(@RequestBody @Valid UsersDto usersDto){
        UsersDto result = usersService.createUsers(usersDto);
        return ResponseEntity.ok(result);
    }
}
