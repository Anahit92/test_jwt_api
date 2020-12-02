package com.company.service;

import com.company.model.User;


public interface UserService {
    void addUser(String phone_number, String id_number, String password);
    String loginUser(String id_number, String password);
	User verifyUser(String token);
}
