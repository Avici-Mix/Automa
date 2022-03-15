package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.vo.LoginUserVO;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.UserVO;
import com.zzb.AutomaArticle.vo.params.LoginParamsVO;

public interface SsoService {
    Result login(LoginParamsVO loginParamsVO);

    SysUser checkToken(String token);

    Result logout(String token);

    /**
     * 注册
     * @param loginParamsVO
     * @return
     */
    Result register(LoginParamsVO loginParamsVO);
}
