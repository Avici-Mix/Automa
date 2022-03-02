package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.Result;
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
    public Result listArticle(@RequestBody PageParamsVO pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     *  首页 最热文章
     * @return
     */
    @PostMapping("hot")
    public Result hotArticle(){
        int limit =5;
        return articleService.hotArticles(limit);
    }

    /**
     *  首页 最新文章
     * @return
     */
    @PostMapping("new")
    public Result newArticles(){
        int limit =5;
        return articleService.newArticles(limit);
    }


    /**
     *  首页 文章归档
     * @return
     */
    @PostMapping("listArchives")
    public Result listArchives(){
        return articleService.listArchives();
    }
}
