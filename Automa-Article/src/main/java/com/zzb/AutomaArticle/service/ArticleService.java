package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.vo.ArticleVO;
import com.zzb.AutomaArticle.vo.ResultVO;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;

import java.util.List;


public interface ArticleService {
    /**
     * 分页查询文章列表
     * @param pageParams
     * @return
     */
    ResultVO listArticle(PageParamsVO pageParams);
}
