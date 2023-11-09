package com.ppn.ppn.service.constract;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.payload.SearchUserRequest;
import com.ppn.ppn.payload.SearchUserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUsersService {
    UsersDto createUsers(UsersDto usersDto);

    boolean verifyUser(String verifyCode);

    List<UsersDto> all(Pageable pageable);

    SearchUserResponse search(SearchUserRequest request, Pageable pageable);
}
