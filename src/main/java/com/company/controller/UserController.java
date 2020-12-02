package com.company.controller;

import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate/login")
    @ResponseBody
    public Map<String, Object> loginUser(@RequestParam("id_number") String id_number,
                                         @RequestParam("password") String password) {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("token", userService.loginUser(id_number, password));
            return response;
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid username or password", e);
        } catch (Throwable e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate/register")
    @ResponseBody
    public void addUser(@RequestParam("phone") String phone,
                        @RequestParam("id_number") String id_number,
                        @RequestParam("password") String password,
                        @RequestParam("re_password") String re_password) {
        try {
            userService.addUser(phone, id_number, password);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid parameter", e);
        } catch (Throwable e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", e);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/authenticate/verify")
    @ResponseBody
    public Map<String, String> verifyUser(@RequestParam("token") String token) {
        try {
            return userService.verifyUser(token);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid parameter", e);
        } catch (Throwable e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", e);
        }
    }

}