package com.ppn.ppn.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class Users extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "email")
    private String email;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "gender")
    private String gender;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "users")
    private List<Payment> payments;

    @OneToMany(mappedBy = "users")
    private List<Car> cars;

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "userId", referencedColumnName = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId", referencedColumnName = "roleId", table = "Role")
    )
    Set<Role> roles;
}
