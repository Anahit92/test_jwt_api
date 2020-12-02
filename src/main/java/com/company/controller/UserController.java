package com.company.controller;

import com.company.model.User;
import com.company.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.POST, value="/authenticate/login")
    @ResponseBody
    public String addUser(@RequestParam("id_number") String id_number,
                          @RequestParam("password") String password) {
        return userService.loginUser(id_number, password);
    }

    @RequestMapping(method = RequestMethod.POST, value="/authenticate/register")
    @ResponseBody
    public void addUser(@RequestParam("phone") String phone,
                          @RequestParam("id_number") String id_number,
                          @RequestParam("password") String password,
                          @RequestParam("re_password") String re_password) {
        userService.addUser(phone, id_number, password);
    }

    @RequestMapping(method = RequestMethod.POST, value="/authenticate/verify")
    @ResponseBody
    public User verifyUser(@RequestParam("token") String token) {
        return userService.verifyUser(token);
    }

}