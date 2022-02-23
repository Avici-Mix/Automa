package com.zzb.AutomaArticle.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultVO {

    private boolean success;

    private int code;

    private String msg;

    private Object data;

    public static ResultVO success(Object data){
        return new ResultVO(true,200,"success",data);
    }

    public static ResultVO fail(int code, String msg){
        return new ResultVO(false,code,msg,null);
    }
}
