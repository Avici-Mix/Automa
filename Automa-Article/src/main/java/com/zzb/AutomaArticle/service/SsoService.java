package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.LoginParamsVO;

public interface SsoService {
    Result login(LoginParamsVO loginParamsVO);

    Result checkToken(String token);

    Result logout(String token);

    /**
     * 注册
     * @param loginParamsVO
     * @return
     */
    Result register(LoginParamsVO loginParamsVO);
}
