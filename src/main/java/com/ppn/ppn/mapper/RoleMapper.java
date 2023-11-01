package com.ppn.ppn.mapper;

import com.ppn.ppn.dto.RoleDto;
import com.ppn.ppn.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role roleDtoToRole(RoleDto roleDto);

    RoleDto roleToRoleDto(Role role);
}
