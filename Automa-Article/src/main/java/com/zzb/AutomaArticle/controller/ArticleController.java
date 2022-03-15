package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.common.aop.LogAnnotation;
import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.ArticleParam;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


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
    @LogAnnotation(module="文章",operator="获取文章列表")
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

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }
}
