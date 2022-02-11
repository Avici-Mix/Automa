package com.zzb.automa.controller;

import com.zzb.automa.bean.Result;
import com.zzb.automa.bean.User;
import com.zzb.automa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping(path ="/register")
    public Result register(@RequestBody User user){
        Result register =  userService.register(user);
        return register;
    }

    @PostMapping(path = "/login")
    public Result login(User user) {
        return userService.login(user);
    }
}
