package com.ppn.ppn.service;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.mapper.UsersMapper;
import com.ppn.ppn.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersServiceImpl implements IUsersService {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UsersMapper usersMapper = UsersMapper.INSTANCE;

    @Override
    public UsersDto createUsers(UsersDto usersDto) {
        Users users = usersMapper.usersDtoUsers(usersDto);
        Users dataSaved = userRepository.save(users);
        return usersMapper.usersToUsersDto(dataSaved);
    }

    public Users checkLogin(Users user) {
        Users userCheck = null;
        if (user.getEmail() != null) {
            userCheck = userRepository.findByEmail(user.getEmail()).get();
        }
        if (userCheck != null && passwordEncoder.matches(user.getPassword(), userCheck.getPassword())) {
            return userCheck;
        }
        return null;
    }

}
