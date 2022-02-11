package com.zzb.automa.service;

import com.zzb.automa.bean.Result;
import com.zzb.automa.bean.User;
import com.zzb.automa.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     *   注册
     * @param user
     * @return
     */
    public Result register(User user) {
        Result result = new Result();
        result.setCode(-1);
        result.setDetail(null);
        try {
            User exitUser = userMapper.findUserByName(user.getUsername());
            if(exitUser!=null){
                result.setCode(-1);
                result.setMsg("用户名已存在");
            }else {
                userMapper.register(user);
                result.setMsg("注册成功");
                result.setCode(200);
                result.setDetail(user);
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 登录
     * @param user
     * @return
     */
    public Result login(User user) {
        Result result = new Result();
        result.setCode(-1);
        result.setDetail(null);

        try {
            Long userId = userMapper.Login(user);
            if(userId == null){
                result.setCode(-1);
                result.setMsg("用户名或密码错误");
            }else{
                result.setCode(200);
                result.setMsg("登录成功");
                user.setId(userId);
                result.setDetail(user);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
