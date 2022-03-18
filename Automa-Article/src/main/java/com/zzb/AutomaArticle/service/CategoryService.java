package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.vo.CategoryVO;
import com.zzb.AutomaArticle.vo.Result;

public interface CategoryService {

    CategoryVO findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result categoryDetailById(Long id);

    Result categoriesDetailById(Long id);
}
