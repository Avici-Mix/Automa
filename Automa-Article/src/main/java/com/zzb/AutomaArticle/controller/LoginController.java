package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.service.SsoService;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.LoginParamsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private SsoService ssoService;


    @PostMapping
    public Result login(@RequestBody LoginParamsVO loginParamsVO){
        return ssoService.login(loginParamsVO);
    }
}
