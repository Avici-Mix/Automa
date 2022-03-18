package com.zzb.AutomaArticle.utils;

import com.zzb.AutomaArticle.dao.pojo.SysUser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserThreadLocal {

    private UserThreadLocal(){}

    private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<>();

   public static void put(SysUser sysUser){
       LOCAL.set(sysUser);
       log.info("线程池中放入用户："+LOCAL.get());
   }

   public static SysUser get(){
       return LOCAL.get();
   }

   public static void remove(){
       LOCAL.remove();
   }

}
