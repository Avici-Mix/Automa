package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.UserVO;

public interface SysUserService {

    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    SysUser findUserByAccount(String account);

    void save(SysUser sysUser);

    UserVO findUserVOById(Long authorId);
}
