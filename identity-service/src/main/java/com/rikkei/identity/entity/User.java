package com.rikkei.identity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false, name = "password_hash")
    private String password;

    @Column(nullable = false, length = 30)
    private String role;

    @Column(name = "permissions")
    private String permissions; // e.g. "PRODUCT_READ,PRODUCT_CREATE"

    protected User() {}

    public User(String id, String username, String password, String role, String permissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.permissions = permissions;
    }

    public List<String> getPermissionsList() {
        if (permissions == null || permissions.isBlank()) return List.of();
        return Arrays.asList(permissions.split(","));
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getPermissions() { return permissions; }
}
