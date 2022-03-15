package com.zzb.AutomaArticle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzb.AutomaArticle.dao.mapper.SysUserMapper;
import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.service.SsoService;
import com.zzb.AutomaArticle.service.SysUserService;
import com.zzb.AutomaArticle.vo.ErrorCode;
import com.zzb.AutomaArticle.vo.LoginUserVO;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl  implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    @Lazy
    private SsoService ssoService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if(sysUser==null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/default_avatar.jpg");
            sysUser.setNickname("自动化之路");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getAccount,SysUser::getId,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        /**
         * 1、token合法性校验
         *       是否为空，解析 是否成功，redis是否存在
         * 2、校验失败，返回错误
         * 3、如果成功，返回对应的结果
         */

        SysUser sysUser = ssoService.checkToken(token);
        if(sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        loginUserVO.setId(sysUser.getId());
        loginUserVO.setAccount(sysUser.getAccount());
        loginUserVO.setAvatar(sysUser.getAvatar());
        loginUserVO.setNickname(sysUser.getNickname());
        return Result.success(loginUserVO);
    }

    @Override
    public SysUser findUserByAccount(String account) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVO findUserVOById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if(sysUser==null){
            sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setAvatar("/default_avatar.jpg");
            sysUser.setNickname("自动化之路");
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(sysUser,userVO);
        userVO.setId(String.valueOf(sysUser.getId()));
        return userVO;
    }
}
