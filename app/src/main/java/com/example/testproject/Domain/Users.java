package com.example.testproject.Domain;

import java.io.Serializable;

public class Users implements Serializable {
    private String id;
    private String email;
    private String role;

    public Users() {
    }

    public Users(String id, String email, String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
