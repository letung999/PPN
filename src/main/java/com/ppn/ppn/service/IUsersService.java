package com.ppn.ppn.service;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Users;

public interface IUsersService {
    UsersDto createUsers(UsersDto usersDto);
}
