package com.zzb.AutomaArticle.vo.params;

import lombok.Data;

@Data
public class ArticleViewParam {
    private int categoryId;

    private int page;

    private int pageSize;
}
