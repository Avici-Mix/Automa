package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.utils.UserThreadLocal;
import com.zzb.AutomaArticle.vo.Result;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class testController {

    @RequestMapping
    public Result test(){
        SysUser sysUser = UserThreadLocal.get();
        return Result.success(sysUser);
    }

}
