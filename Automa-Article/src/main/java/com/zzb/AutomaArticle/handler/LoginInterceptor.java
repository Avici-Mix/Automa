package com.zzb.AutomaArticle.handler;

import com.alibaba.fastjson.JSON;
import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.service.SsoService;
import com.zzb.AutomaArticle.utils.UserThreadLocal;
import com.zzb.AutomaArticle.vo.ErrorCode;
import com.zzb.AutomaArticle.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private SsoService ssoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器");

//        在执行controller方法（Handler）之前执行
        /**
         * 1、需要判断请求的接口路径，是否为 HandlerMethod（controller）
         * 2、判断token是否为空，为空则未登录
         * 3、不为空，登录验证
         * 4、认证成功放行
         */
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        String token = request.getHeader("Authorization");
        log.info("=================request start===========================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}", token);
        log.info("=================request end===========================");
        if(StringUtils.isBlank(token)){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }
        SysUser sysUser = ssoService.checkToken(token);
        if(sysUser == null){
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().println(JSON.toJSONString(result));
            return false;
        }

        UserThreadLocal.put(sysUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        如果不删除 ThreadLocal 中用完的信息，会有内存泄漏的风险
        UserThreadLocal.remove();
    }
}
