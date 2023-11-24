package com.ppn.ppn.service;


import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Role;
import com.ppn.ppn.entities.Users;
import com.ppn.ppn.exception.ResourceDuplicateException;
import com.ppn.ppn.exception.ResourcesNotFoundException;
import com.ppn.ppn.mapper.UsersMapper;
import com.ppn.ppn.payload.SearchUserRequest;
import com.ppn.ppn.payload.SearchUserResponse;
import com.ppn.ppn.repository.CacheDataRepository;
import com.ppn.ppn.repository.RoleRepository;
import com.ppn.ppn.repository.UsersRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.ppn.ppn.constant.ApprovalStatus.ACTIVE;
import static com.ppn.ppn.constant.ApprovalStatus.PENDING;
import static com.ppn.ppn.constant.RoleConstant.VIEWER;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {

    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CacheDataRepository cacheDataRepository;
    @InjectMocks
    private UsersServiceImpl usersService;
    @Spy
    private UsersMapper usersMapper = UsersMapper.INSTANCE;
    @Mock
    private PasswordEncoder passwordEncoder;

    private Users users;
    private Role role;
    private UsersDto usersDto;
    private Set<Role> roles = new HashSet<>();

    @BeforeEach
    public void setup() {

        role = Role.builder()
                .roleId(1)
                .roleName(VIEWER)
                .build();
        roles.add(role);

        usersDto = UsersDto.builder()
                .userId(1)
                .email("c@gmail.com")
                .firstName("tung")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("0338257409")
                .status(String.valueOf(PENDING))
                .roles(roles)
                .build();
        users = usersMapper.usersDtoUsers(usersDto);

        users.setStatus(String.valueOf(PENDING));
        users.setVerifyCode(randomString(30));
        users.setPassword(passwordEncoder.encode(usersDto.getPassword()));
        users.setRoles(roles);
    }

    @Test
    public void givenUsersObject_whenCreateUsers_thenUsersObject() {

        BDDMockito.given(usersRepository.findByEmail(usersDto.getEmail()))
                .willReturn(Optional.empty());
        //action
        BDDMockito.given(roleRepository.findByRoleName(VIEWER))
                .willReturn(Optional.of(role));

        BDDMockito.when(usersRepository.save(any())).thenReturn(users);

        UsersDto dataSave = usersService.createUsers(usersMapper.usersToUsersDto(users));

        //output
        Assertions.assertThat(dataSave).isNotNull();
    }

    @Test
    public void givenExistingEmail_whenCreateUsers_thenThrowResourceDuplicateException() {
        //action
        BDDMockito.given(usersRepository.findByEmail(usersDto.getEmail()))
                .willReturn(Optional.of(users));
        //output
        org.junit.jupiter.api.Assertions.assertThrows(ResourceDuplicateException.class, () -> {
            usersService.createUsers(usersDto);
        });

        BDDMockito.verify(roleRepository, Mockito.never()).findByRoleName(VIEWER);
        BDDMockito.verify(usersRepository, Mockito.never()).save(any());
    }

    @Test
    public void givenRoleHasNull_whenCreateUsers_thenThrowResourceNotFoundException() {
        //action
        BDDMockito.given(usersRepository.findByEmail(usersDto.getEmail()))
                .willReturn(Optional.empty());

        BDDMockito.given(roleRepository.findByRoleName(VIEWER))
                .willReturn(Optional.empty());
        //output
        org.junit.jupiter.api.Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
            usersService.createUsers(usersDto);
        });

        BDDMockito.verify(usersRepository, Mockito.never()).save(any(Users.class));
    }

    @Test
    public void givenUsersObjectModified_whenVerifyUser_thenReturnTrue() {
        //setup
        BDDMockito.given(usersRepository.findByVerifyCode(users.getVerifyCode()))
                .willReturn(Optional.of(users));
        //action
        users.setStatus(String.valueOf(ACTIVE));

        BDDMockito.given(usersRepository.save(any(Users.class))).willReturn(users);

        Boolean result = usersService.verifyUser(users.getVerifyCode());

        //output
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    public void givenUsersObjectHasNull_whenVerifyUser_thenThrowResourceNotFoundException() {
        //setup
        BDDMockito.given(usersRepository.findByVerifyCode(users.getVerifyCode()))
                .willReturn(Optional.empty());

        //output
        org.junit.jupiter.api.Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
            usersService.verifyUser(users.getVerifyCode());
        });

        BDDMockito.verify(usersRepository, BDDMockito.never()).save(any(Users.class));
    }

    @Test
    public void givenUsersList_whenGetAll_thenUsersList() {
        //setup
        Page<Users> usersPage = BDDMockito.mock(Page.class);

        Mockito.doReturn(getMockListUsers())
                .when(usersPage).getContent();

        BDDMockito.given(usersRepository.findAll(any(Pageable.class)))
                .willReturn(usersPage);

        //action
        List<UsersDto> usersDtoList = usersService.all(PageRequest.of(0, 1));

        //output
        Assertions.assertThat(usersDtoList).isNotNull();
    }

    @Test
    public void givenAllListUsers_whenSearchWithoutFilter_thenReturnAllListUsers() {
        //setup
        SearchUserRequest searchRequest = new SearchUserRequest();
        searchRequest.setPageIndex(1);
        searchRequest.setPageSize(10);

        //action
        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Users> usersPage = Mockito.mock(Page.class);

        //action
        Mockito.doReturn(usersPage)
                .when(usersRepository).findAll(any(Specification.class), any(Pageable.class));

        Mockito.doReturn(getMockListUsers()).when(usersPage).getContent();

        SearchUserResponse searchUserResponse = usersService.search(searchRequest, pageable);
        //output
        Assertions.assertThat(searchUserResponse).isNotNull();
        Assertions.assertThat(searchUserResponse.getUserDtoList().size()).isEqualTo(3);
    }

    @Test
    public void givenSearchUserResponse_whenSearch_thenSearchUserResponse() {
        //setup
        SearchUserRequest searchRequest = new SearchUserRequest();
        searchRequest.setPageIndex(1);
        searchRequest.setPageSize(10);
        searchRequest.setSortBy("firstName");
        searchRequest.setAscending(true);
        searchRequest.setEmail("test@example.com");
        searchRequest.setFirstName("John");
        searchRequest.setPhoneNumber("+1234567890");
        searchRequest.setStatus("Active");
        searchRequest.setGender("Male");

        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Users> usersPage = Mockito.mock(Page.class);

        //action
        Mockito.doReturn(usersPage)
                .when(usersRepository).findAll(any(Specification.class), any(Pageable.class));

        Mockito.doReturn(getMockListUsers()).when(usersPage).getContent();

        SearchUserResponse searchUserResponse = usersService.search(searchRequest, pageable);
        //output
        Assertions.assertThat(searchUserResponse).isNotNull();
        Assertions.assertThat(searchUserResponse.getUserDtoList().size()).isEqualTo(3);
    }

    @Test
    public void givenEmptyListUsers_whenSearch_thenEmptyListUsers() {
        //setup
        SearchUserRequest searchRequest = new SearchUserRequest();
        searchRequest.setPageIndex(1);
        searchRequest.setPageSize(10);
        searchRequest.setSortBy("firstName");
        searchRequest.setAscending(true);
        searchRequest.setEmail("test@example.com");
        searchRequest.setFirstName("John");
        searchRequest.setPhoneNumber("+1234567890");
        searchRequest.setStatus("Active");
        searchRequest.setGender("Male");

        Pageable pageable = Mockito.mock(Pageable.class);
        Page<Users> usersPage = Mockito.mock(Page.class);

        //action
        Mockito.doReturn(usersPage).when(usersRepository)
                .findAll(any(Specification.class), any(Pageable.class));

        Mockito.doReturn(Collections.emptyList()).when(usersPage).getContent();

        SearchUserResponse searchUserResponse = usersService.search(searchRequest, pageable);

        //output
        Assertions.assertThat(searchUserResponse.getUserDtoList().size()).isEqualTo(0);
        Assertions.assertThat(searchUserResponse.getNumOfItems()).isEqualTo(0);
        Assertions.assertThat(searchUserResponse.getNumOfPage()).isEqualTo(0);
    }

    @Test
    public void givenNullObject_whenUpdateUsers_thenThrowResourceNotFoundException() {
        //setup
        Mockito.when(usersRepository.findById(usersDto.getUserId())).thenReturn(Optional.empty());

        //output
        org.junit.jupiter.api.Assertions.assertThrows(ResourcesNotFoundException.class, () -> {
            usersService.updateUsers(usersDto);
        });

        Mockito.verify(usersRepository, Mockito.never()).findAll();
        Mockito.verify(usersRepository, Mockito.never()).save(users);
        Mockito.verify(cacheDataRepository, Mockito.never()).findAll();
        Mockito.verify(cacheDataRepository, Mockito.never()).deleteAllById(any());
    }

    @Test
    public void given_whenUpdateUsers_thenResourceDuplicateException() {
        //setup
        Users userExisted = Users.builder()
                .email("b@gmail.com")
                .firstName("tung")
                .password(passwordEncoder.encode("123456"))
                .phoneNumber("0338257409")
                .status(String.valueOf(PENDING))
                .roles(roles)
                .build();

        Mockito.when(usersRepository.findById(usersDto.getUserId()))
                .thenReturn(Optional.of(userExisted));
        Mockito.doReturn(getMockListUsers()).when(usersRepository).findAll();

        //action and output
        org.junit.jupiter.api.Assertions.assertThrows(ResourceDuplicateException.class, () -> {
            usersService.updateUsers(usersDto);
        });
        Mockito.verify(usersRepository, Mockito.never()).save(users);
        Mockito.verify(cacheDataRepository, Mockito.never()).findAll();
        Mockito.verify(cacheDataRepository, Mockito.never()).deleteAllById(any());

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

    private List<Users> getMockListUsers() {
        return List.of(
                Users.builder()
                        .userId(1)
                        .email("a@gmail.com")
                        .firstName("tung")
                        .password(passwordEncoder.encode("123456"))
                        .phoneNumber("0338257409")
                        .status(String.valueOf(PENDING))
                        .roles(roles)
                        .build(),
                Users.builder()
                        .userId(2)
                        .email("b@gmail.com")
                        .firstName("tung")
                        .password(passwordEncoder.encode("123456"))
                        .phoneNumber("0338257409")
                        .status(String.valueOf(PENDING))
                        .roles(roles)
                        .build(),
                Users.builder()
                        .userId(3)
                        .email("c@gmail.com")
                        .firstName("tung")
                        .password(passwordEncoder.encode("123456"))
                        .phoneNumber("0338257409")
                        .status(String.valueOf(PENDING))
                        .roles(roles)
                        .build()
        );
    }

    private List<String> getMockListEmailData() {
        List<String> emails = Arrays.asList(
                "user1@example.com",
                "user1@example.com",
                "user1@example.com",
                "user1@example.com",
                "user1@example.com"
        );
        return emails;
    }

    private SearchUserResponse getMockDataSearchUserResponse() {
        List<UsersDto> usersList = new ArrayList<>();
        UsersDto user1 = new UsersDto();
        user1.setFirstName("John");
        user1.setEmail("test@example.com");
        user1.setPhoneNumber("+1234567890");
        user1.setGender("Male");
        user1.setStatus("PENDING");

        UsersDto user2 = new UsersDto();
        user2.setFirstName("Jane");
        user2.setEmail("jane@example.com");
        user2.setPhoneNumber("+9876543210");
        user2.setGender("Female");
        user2.setStatus("PENDING");

        usersList.add(user1);
        usersList.add(user2);

        SearchUserResponse searchUserResponse = SearchUserResponse.builder()
                .numOfItems((long) usersList.size())
                .numOfPage(1)
                .userDtoList(usersList)
                .build();

        return searchUserResponse;
    }
}
