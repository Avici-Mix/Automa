package com.zzb.AutomaArticle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzb.AutomaArticle.dao.mapper.CategoryMapper;
import com.zzb.AutomaArticle.dao.pojo.Category;
import com.zzb.AutomaArticle.service.CategoryService;
import com.zzb.AutomaArticle.vo.CategoryVO;
import com.zzb.AutomaArticle.vo.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVO findCategoryById(Long categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        CategoryVO categoryVo = new CategoryVO();
        BeanUtils.copyProperties(category,categoryVo);
        categoryVo.setId(String.valueOf(category.getId()));
        return categoryVo;
    }

    @Override
    public Result findAll() {
        List<Category> categories = this.categoryMapper.selectList(new LambdaQueryWrapper<>());
        return Result.success(copyList(categories));
    }

    private List<CategoryVO> copyList(List<Category> categories) {
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        for (Category category:categories) {
            categoryVOList.add(copy(category));
        }

        return categoryVOList;
    }

    private CategoryVO copy(Category category) {
        CategoryVO categoryVO = new CategoryVO();
        BeanUtils.copyProperties(category,categoryVO);
        categoryVO.setId(String.valueOf(category.getId()));
        return categoryVO;
    }

    @Override
    public Result findAllDetail() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        //页面交互的对象
        return Result.success(copyList(categories));
    }

    @Override
    public Result categoryDetailById(Long id) {
        return null;
    }

    @Override
    public Result categoriesDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryVO categoryVo = copy(category);
        return Result.success(categoryVo);
    }
}
