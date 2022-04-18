package com.zzb.AutomaArticle.vo.params;

import lombok.Data;

@Data
public class CommentParam {
    private Long articleId;

    private String content;

    private Long parent;

    private Long toUserId;

    private int articleCategoryId;

    private int articlePage;

    private int articlePageSize;
}
