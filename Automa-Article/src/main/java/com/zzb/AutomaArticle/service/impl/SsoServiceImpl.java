package com.zzb.AutomaArticle.service.impl;


import com.alibaba.fastjson.JSON;
import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.service.SsoService;
import com.zzb.AutomaArticle.service.SysUserService;
import com.zzb.AutomaArticle.utils.JWTUtils;
import com.zzb.AutomaArticle.vo.ErrorCode;
import com.zzb.AutomaArticle.vo.LoginUserVO;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.LoginParamsVO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class SsoServiceImpl implements SsoService {

    @Value("${effectiveDate}")
    private int effectiveDate;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String slat = "mszlu!@#";

    @Override
    public Result login(LoginParamsVO loginParamsVO) {
        /**
         * 检查参数是否合法
         * 根据用户名和密码去表中查询是否存在
         * 不存在 登录失败
         * 存在使用jwt生成token返回前端
         * token放入redis当中，redis token：user信息设置过期时间（
         * 登录认证的时候先验证token字符串是否合法，去redis认证是否存在）
         */

        String account = loginParamsVO.getAccount();
        String password = loginParamsVO.getPassword();
        if(StringUtils.isBlank(account)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password+slat);
        SysUser sysUser =  sysUserService.findUser(account,password);
        if(sysUser  == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),effectiveDate, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result checkToken(String token) {
        if(StringUtils.isBlank(token)){
           return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken((token));
        if(stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if(StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(sysUser.getId());
        loginUserVO.setAccount(sysUser.getAccount());
        loginUserVO.setAvatar(sysUser.getAccount());
        loginUserVO.setNickname(sysUser.getNickname());
        return Result.success(loginUserVO);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParamsVO loginParamsVO) {
        /**
         * 1、判断参数合法
         * 2、判断账号是否存在，存在，返回账户已经被注册
         * 3、如果账户不存在，注册用户
         * 4、生成token
         * 5、存入redis，并返回
         * 6、加入事物，如上述过程有问题，回滚
         */
        String account = loginParamsVO.getAccount();
        String password = loginParamsVO.getPassword();
        String nickname = loginParamsVO.getNickname();
        if (StringUtils.isBlank(account)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(nickname)
        ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser =  sysUserService.findUserByAccount(account);
        if(sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        sysUser = new SysUser();
        sysUser.setNickname(nickname);
        sysUser.setAccount(account);
        sysUser.setPassword(DigestUtils.md5Hex(password+slat));
        sysUser.setCreateDate(System.currentTimeMillis());
        sysUser.setLastLogin(System.currentTimeMillis());
        sysUser.setAvatar("/static/img/logo.b3a48c0.png");
        sysUser.setAdmin(1); //1 为true
        sysUser.setDeleted(0); // 0 为false
        sysUser.setSalt("");
        sysUser.setStatus("");
        sysUser.setEmail("");
        this.sysUserService.save(sysUser);

        String token = JWTUtils.createToken(sysUser.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),effectiveDate, TimeUnit.DAYS);
        return Result.success(token);
    }
}
