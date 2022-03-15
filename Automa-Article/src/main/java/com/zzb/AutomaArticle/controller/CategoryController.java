package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.service.CategoryService;
import com.zzb.AutomaArticle.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result listCategory(){
        return categoryService.findAll();
    }
}
