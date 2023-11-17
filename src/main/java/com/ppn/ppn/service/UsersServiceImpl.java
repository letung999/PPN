package com.ppn.ppn.service;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.CacheData;
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
import com.ppn.ppn.service.constract.IUsersService;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private CacheDataRepository cacheDataRepository;

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

    @Override
    public List<UsersDto> all(Pageable pageable) {
        Page<Users> usersPage = userRepository.findAll(pageable);
        List<Users> usersList = usersPage.getContent();
        if (usersList.size() == 0) {
            return new ArrayList<UsersDto>();
        }
        List<UsersDto> usersDtoList = usersList.stream().map(users -> {
            UsersDto usersDto = new UsersDto();
            usersDto.setUserId(users.getUserId());
            usersDto.setFirstName(users.getFirstName());
            usersDto.setCars(users.getCars());
            usersDto.setPhoneNumber(users.getPhoneNumber());
            usersDto.setGender(users.getGender());
            usersDto.setEmail(users.getEmail());
            usersDto.setStatus(users.getStatus());
            usersDto.setPayments(users.getPayments());
            usersDto.setRoles(users.getRoles());
            return usersDto;
        }).collect(Collectors.toList());

        return usersDtoList;
    }

    @Override
    public SearchUserResponse search(SearchUserRequest request, Pageable pageable) {
        Specification<Users> spec = buildSearchUserFilter(request);
        Page<Users> usersPage = userRepository.findAll(spec, pageable);
        List<Users> usersData = usersPage.getContent();
        //mapper Data
        List<UsersDto> usersDtos = usersData.stream().map(users -> {
            UsersDto usersDto = new UsersDto();
            usersDto.setEmail(users.getEmail());
            usersDto.setPhoneNumber(users.getPhoneNumber());
            usersDto.setPayments(users.getPayments());
            usersDto.setFirstName(users.getFirstName());
            usersDto.setStatus(users.getStatus());
            usersDto.setCars(users.getCars());
            usersDto.setGender(users.getGender());
            usersDto.setCreatedDate(users.getCreatedDate());
            usersDto.setUpdatedDate(users.getUpdatedDate());
            return usersDto;
        }).collect(Collectors.toList());

        SearchUserResponse searchUserResponse = SearchUserResponse.builder()
                .userDtoList(usersDtos)
                .numOfItems(usersPage.getTotalElements())
                .numOfPage(usersPage.getTotalPages())
                .build();

        return searchUserResponse;
    }

    @Override
    public UsersDto updateUsers(UsersDto usersDto) {
        Optional<Users> users = userRepository.findById(usersDto.getUserId());
        if (users.isEmpty()) {
            throw new ResourcesNotFoundException("emails", usersDto.getEmail());
        }

        List<String> emails = userRepository.findAll().stream()
                .filter(u -> !u.getEmail().equals(users.get().getEmail()))
                .map(u -> {
                    return u.getEmail();
                }).collect(Collectors.toList());

        if (emails.contains(usersDto.getEmail())) {
            throw new ResourceDuplicateException("email", usersDto.getEmail());
        }
        Users dataSave = userRepository.save(usersMapper.usersDtoUsers(usersDto));

        //deleted Cache for users:
        List<CacheData> cacheDataList = (List<CacheData>) cacheDataRepository.findAll();
        List<String> cacheKeyList = cacheDataList.stream()
                .map(CacheData::getKey)
                .filter(k -> k.substring(0, k.indexOf("-")).toLowerCase()
                        .matches(".*" + "users" + ".*"))
                .collect(Collectors.toList());
        cacheDataRepository.deleteAllById(cacheKeyList);

        return usersMapper.usersToUsersDto(dataSave);
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

    private Specification<Users> buildSearchUserFilter(SearchUserRequest request) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> finalFilter = new ArrayList<>();
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                finalFilter.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
            }
            if (request.getGender() != null && !request.getGender().isEmpty()) {
                finalFilter.add(criteriaBuilder.equal(root.get("gender"), request.getGender()));
            }
            if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
                finalFilter.add(criteriaBuilder.equal(root.get("phoneNumber"), request.getPhoneNumber()));
            }
            if (request.getStatus() != null && !request.getStatus().isEmpty()) {
                finalFilter.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                finalFilter.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("firstName")), "%" + request.getFirstName().toLowerCase() + "%"));
            }
            if (request.isAscending() && request.getSortBy() != null && !request.getSortBy().isEmpty()) {
                query.orderBy(criteriaBuilder.asc(root.get(request.getSortBy())));
            }
            if (!request.isAscending() && request.getSortBy() != null && !request.getSortBy().isEmpty()) {
                query.orderBy(criteriaBuilder.desc(root.get(request.getSortBy())));
            }
            return criteriaBuilder.and(finalFilter.toArray(new Predicate[0]));
        });
    }
}
