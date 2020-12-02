package com.company.service;

import java.util.Map;

public interface UserService {
    void addUser(String phone_number, String id_number, String password) throws Throwable;

    String loginUser(String id_number, String password) throws Throwable;

    Map<String, String> verifyUser(String token) throws Throwable;
}
