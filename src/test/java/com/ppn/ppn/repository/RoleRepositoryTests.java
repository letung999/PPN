package com.ppn.ppn.repository;


import com.ppn.ppn.entities.Role;
import com.ppn.ppn.entities.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.ppn.ppn.constant.RoleConstant.VIEWER;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UsersRepository usersRepository;

    @BeforeEach
    public void setup() {
        //setup
        Users users = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .build();
        Users dataSave = usersRepository.save(users);

        Set<Users> usersSet = new HashSet<>();
        usersSet.add(dataSave);

        Role role = Role.builder()
                .roleName(VIEWER)
                .users(usersSet)
                .build();

        roleRepository.save(role);
    }

    @Test
    public void givenRoleObject_whenFindByRoleName_thenRoleObject() {

        //action
        Optional<Role> role = roleRepository.findByRoleName(VIEWER);

        //output
        Assertions.assertThat(role).isNotNull();
        Assertions.assertThat(role.get().getRoleName()).isEqualTo(VIEWER);
    }
}
