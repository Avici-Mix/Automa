package com.zzb.AutomaArticle.service;


import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.ArticleParam;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;

import java.util.List;


public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    Result listArticle(PageParamsVO pageParams);

    /**
     * 最热文章
     * @param limit
     * @return
     */
    Result hotArticles(int limit);

    /**
     * 最信文章
     * @param limit
     * @return
     */
    Result newArticles(int limit);

    Result listArchives();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
