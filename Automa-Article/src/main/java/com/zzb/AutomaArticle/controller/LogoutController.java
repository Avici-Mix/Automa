package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.service.SsoService;
import com.zzb.AutomaArticle.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("logout")
public class LogoutController {

    @Autowired
    private SsoService ssoService;


    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token) {
        return ssoService.logout(token);
    }
}
