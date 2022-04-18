package com.zzb.AutomaArticle.vo;

import com.zzb.AutomaArticle.vo.params.ArticleViewParam;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleCashMessage  implements Serializable {
    private ArticleViewParam articleViewParam;
    private String redisKey;
}
