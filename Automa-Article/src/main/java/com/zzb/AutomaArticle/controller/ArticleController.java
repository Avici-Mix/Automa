package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.ResultVO;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     *  首页 文章列表
     * @param pageParams
     * @return
     */
    @PostMapping
    public ResultVO listArticle(@RequestBody PageParamsVO pageParams){
        return articleService.listArticle(pageParams);
    }
}
