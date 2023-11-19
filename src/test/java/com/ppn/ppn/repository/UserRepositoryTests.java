package com.ppn.ppn.repository;


import com.ppn.ppn.entities.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {
    @Autowired
    private UsersRepository usersRepository;


    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {
        //set up
        Users users = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .build();

        //action
        Users dataSaved = usersRepository.save(users);

        //output
        Assertions.assertThat(dataSaved).isNotNull();
        Assertions.assertThat(dataSaved.getUserId()).isGreaterThan(0);
    }

    @Test
    public void givenUsersList_whenFindAll_thenUsersList() {
        //setup
        Users users = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .build();

        Users users1 = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .build();

        usersRepository.save(users);
        usersRepository.save(users1);

        //action
        List<Users> usersList = usersRepository.findAll();

        //output
        Assertions.assertThat(usersList).isNotNull();
        Assertions.assertThat(usersList.size()).isEqualTo(3);
    }

    @Test
    public void givenUsersObject_whenFindByUsersById_thenUsersObject() {
        //setup
        Users users = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .build();

        Users dataSaved = usersRepository.save(users);

        //action
        Users data = usersRepository.findById(dataSaved.getUserId()).get();

        //output
        Assertions.assertThat(data).isNotNull();
    }

    @Test
    public void givenUsersObject_whenFindByEmail_thenUsersObject() {
        //setup
        Users users = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .build();

        usersRepository.save(users);

        //action
        Optional<Users> data = usersRepository.findByEmail(users.getEmail());

        //output
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.get().getEmail()).isEqualTo(users.getEmail());
    }

    @Test
    public void givenUsersObject_whenFindByVerifyCode_thenUserObject() {
        //setup
        Users users = Users.builder()
                .email("letung012000@gmail.com")
                .firstName("tung")
                .phoneNumber("0338257409")
                .password("123456")
                .status("PENDING")
                .gender("Male")
                .verifyCode("C7jgrUGYJNsmvrbGXQSf8SZXPsxlvx")
                .build();

        usersRepository.save(users);

        //action
        Optional<Users> data = usersRepository.findByVerifyCode(users.getVerifyCode());

        //output
        Assertions.assertThat(data).isNotNull();
    }
}
