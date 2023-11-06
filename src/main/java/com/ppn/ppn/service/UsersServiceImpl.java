package com.ppn.ppn.service;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Role;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.exception.ResourceDuplicateException;
import com.ppn.ppn.exception.ResourcesNotFoundException;
import com.ppn.ppn.mapper.UsersMapper;
import com.ppn.ppn.repository.RoleRepository;
import com.ppn.ppn.repository.UsersRepository;
import com.ppn.ppn.service.constract.IUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import static com.ppn.ppn.constant.ApprovalStatus.ACTIVE;
import static com.ppn.ppn.constant.ApprovalStatus.PENDING;
import static com.ppn.ppn.constant.RoleConstant.VIEWER;

@Service
@Slf4j
public class UsersServiceImpl implements IUsersService {
    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private UsersMapper usersMapper = UsersMapper.INSTANCE;

    @Override
    public UsersDto createUsers(UsersDto usersDto) {
        Optional<Users> usersEntity = userRepository.findByEmail(usersDto.getEmail());
        if (usersEntity.isPresent()) {
            throw new ResourceDuplicateException("Email", usersDto.getEmail());
        }

        Optional<Role> role = roleRepository.findByRoleName(VIEWER);
        if (role.isEmpty()) {
            throw new ResourcesNotFoundException("Role", VIEWER);
        }
        Set<Role> roles = new HashSet<>();
        roles.add(role.get());

        //set data
        Users users = usersMapper.usersDtoUsers(usersDto);
        users.setStatus(String.valueOf(PENDING));
        users.setVerifyCode(randomString(30));
        users.setPassword(passwordEncoder.encode(usersDto.getPassword()));

        users.setRoles(roles);
        Users dataSaved = userRepository.save(users);
        return usersMapper.usersToUsersDto(dataSaved);
    }

    @Override
    public boolean verifyUser(String verifyCode) {
        Optional<Users> users = userRepository.findByVerifyCode(verifyCode);
        if (users.isEmpty()) {
            throw new ResourcesNotFoundException("users", verifyCode);
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
            int index = random.nextInt(data.length()) + 0;
            resultData.append(data.charAt(index));
        }
        return resultData.toString();
    }

}
