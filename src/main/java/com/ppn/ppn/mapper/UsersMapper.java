package com.ppn.ppn.mapper;

import com.ppn.ppn.dto.UsersDto;
import com.ppn.ppn.entities.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsersMapper {
    UsersMapper INSTANCE = Mappers.getMapper(UsersMapper.class);

    UsersDto usersToUsersDto(Users users);

    Users usersDtoUsers(UsersDto usersDto);
}
