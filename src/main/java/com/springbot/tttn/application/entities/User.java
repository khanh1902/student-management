package com.springbot.tttn.application.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "user_name", nullable = false, unique = true)
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "is_active")
    private Boolean isActive;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id"), // khóa ngoại chính là userid trỏ tới class hiện tại (User)
            inverseJoinColumns = @JoinColumn(name = "role_id")) // Khóa ngoại thứ 2 trỏ tới thuộc tính ở dưới (Role)
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Student student;


    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public User(UUID userId, String username, String fullName, String password, String email, Set<Role> roles) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.isActive = true;
    }

    public User(UUID userId, String username, String fullName, String email, Set<Role> roles) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.roles = roles;
        this.isActive = true;
    }

    @Override
    public String toString() {
        return "[userId='" + userId + '\'' + ", userName='" + username + '\'' + "]";
    }
}