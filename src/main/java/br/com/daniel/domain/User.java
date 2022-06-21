package br.com.daniel.domain;

import java.util.Date;

public class User extends Entity {
    private String name;
    private String email;
    private String password;

    public User(final String id, final Date createdAt, final String createdBy) {
        super(id, createdAt, createdBy);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
