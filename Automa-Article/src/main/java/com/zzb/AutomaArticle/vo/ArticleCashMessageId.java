package com.zzb.AutomaArticle.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleCashMessageId extends ArticleCashMessage {
    private Long articleId;

    private Boolean isDeleteList;
}
