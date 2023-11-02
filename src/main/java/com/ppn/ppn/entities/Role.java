package com.ppn.ppn.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Table(name = "Role")
@Entity
@Getter
@Setter
public class Role extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Integer roleId;

    @Column(name = "roleName")
    private String roleName;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Set<Users> users;
}
