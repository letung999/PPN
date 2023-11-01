package com.ppn.ppn.dto;

import com.ppn.ppn.entities.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleDto extends BaseEntityDto{
    private Integer roleId;
    private String roleName;
    private Set<Users> users;
}
