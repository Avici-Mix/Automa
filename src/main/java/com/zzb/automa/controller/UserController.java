package com.zzb.automa.controller;

import com.zzb.automa.bean.Result;
import com.zzb.automa.bean.User;
import com.zzb.automa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value="/register")
    public Result register(User user){
        Result register =  userService.register(user);
        return register;
    }

    @PostMapping(value = "/login")
    public Result login(User user) {
        return userService.login(user);
    }
}
