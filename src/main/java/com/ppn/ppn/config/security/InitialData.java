package com.ppn.ppn.config.security;

import com.ppn.ppn.entities.Role;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.repository.RoleRepository;
import com.ppn.ppn.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class InitialData implements ApplicationRunner {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            Role role = new Role();
            role.setRoleName("System Admin");
            roleRepository.save(role);

            roles = roleRepository.findAll();
            String pwdEncrypt = passwordEncoder.encode("123456");
            Users user = new Users();
            user.setEmail("systemadmin@gmail.com");
            user.setPassword(pwdEncrypt);
            user.setRoles(new HashSet<>(roles));
            usersRepository.save(user);
        }
    }

}
