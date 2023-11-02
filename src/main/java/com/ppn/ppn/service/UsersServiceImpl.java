package com.ppn.ppn.service;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.mapper.UsersMapper;
import com.ppn.ppn.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements IUsersService {
    @Autowired
    private UsersRepository userRepository;
    private UsersMapper usersMapper = UsersMapper.INSTANCE;

    @Override
    public UsersDto createUsers(UsersDto usersDto) {
        Users users = usersMapper.usersDtoUsers(usersDto);
        Users dataSaved = userRepository.save(users);
        return usersMapper.usersToUsersDto(dataSaved);
    }
}
