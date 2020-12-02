package com.company.model;

import java.util.UUID;

public class User {

    private UUID id;
    private String id_number;
    private String phone;
    private String password;
    private String token;

	public User(){
	}

    public User(UUID id, String id_number, String phone, String password, String token) {
        this.id = id;
        this.id_number = id_number;
        this.phone = phone;
        this.password = password;
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
