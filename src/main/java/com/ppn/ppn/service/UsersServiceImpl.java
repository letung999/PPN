package com.ppn.ppn.service;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.mapper.UsersMapper;
import com.ppn.ppn.repository.UsersRepository;
import com.ppn.ppn.service.constract.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import static com.ppn.ppn.constant.ApprovalStatus.ACTIVE;
import static com.ppn.ppn.constant.ApprovalStatus.PENDING;

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
        users.setStatus(String.valueOf(PENDING));
        users.setVerifyCode(randomString(20));
        Users dataSaved = userRepository.save(users);
        return usersMapper.usersToUsersDto(dataSaved);
    }

    @Override
    public boolean verifyUser(String verifyCode) {
        Optional<Users> users = userRepository.findByVerifyCode(verifyCode);
        if (Objects.isNull(users)) {
            return false;
        }
        users.get().setStatus(String.valueOf(ACTIVE));
        userRepository.save(users.get());
        return true;
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

    //private methods
    private String randomString(int length) {
        String data = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";
        Random random = new Random();
        StringBuilder resultData = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            int index = random.nextInt(length) + 0;
            resultData.append(data.charAt(index));
        }
        return resultData.toString();
    }

}
