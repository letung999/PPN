package com.ppn.ppn.service.constract;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Users;

public interface IUsersService {
    UsersDto createUsers(UsersDto usersDto);
    boolean verifyUser(String verifyCode);
}
