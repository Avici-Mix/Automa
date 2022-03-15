package com.zzb.AutomaArticle.vo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.List;

@Data
public class CommentVO {
    //防止前端 精度损失 把id转为string
//    @JsonSerialize(using = ToStringSerializer.class)
    private String id;

    private UserVO author;

    private String content;

    private List<CommentVO> childrens;

    private String createDate;

    private Integer level;

    private UserVO toUser;
}
