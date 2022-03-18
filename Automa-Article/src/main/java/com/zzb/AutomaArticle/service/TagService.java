package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.TagVO;

import java.util.List;

public interface TagService {

    List<TagVO> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAllDetail();

    Result findDetailById(Long id);

    Result findAll();
}
