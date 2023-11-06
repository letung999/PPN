package com.ppn.ppn.service;

import com.ppn.ppn.dto.RoleDto;
import com.ppn.ppn.entities.Role;
import com.ppn.ppn.exception.ResourceDuplicateException;
import com.ppn.ppn.exception.RoleInputException;
import com.ppn.ppn.mapper.RoleMapper;
import com.ppn.ppn.repository.RoleRepository;
import com.ppn.ppn.service.constract.IRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.ppn.ppn.constant.RoleConstant.*;

@Slf4j
@Service
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;

    private RoleMapper roleMapper = RoleMapper.INSTANCE;

    @Override
    public RoleDto create(RoleDto roleDto) {
        Optional<Role> role = roleRepository.findByRoleName(roleDto.getRoleName());
        if (role.isPresent()) {
            throw new ResourceDuplicateException("Role", roleDto.getRoleName());
        }
        List<String> nameRoles = Arrays.asList(SYSTEM_ADMIN, VIEWER, OWNER, CONTRIBUTE, EDITOR, ADMIN);
        if (!nameRoles.contains(roleDto.getRoleName())) {
            throw new RoleInputException(roleDto.getRoleName());
        }
        Role dataSave = roleRepository.save(roleMapper.roleDtoToRole(roleDto));
        return roleMapper.roleToRoleDto(dataSave);
    }
}
