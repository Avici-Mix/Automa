package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.common.aop.LogAnnotation;
import com.zzb.AutomaArticle.common.cache.Cache;
import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.ArticleParam;
import com.zzb.AutomaArticle.vo.params.ArticleViewParam;
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
    @Cache(expire = 5 * 60 * 1000,name = "listArticle")
    public Result listArticle(@RequestBody PageParamsVO pageParams){
        return articleService.listArticle(pageParams);
    }

    @PostMapping("tag")
    public Result listArticleByTag(@RequestBody PageParamsVO pageParams){
        return articleService.listArticle(pageParams);
    }

    /**
     *  首页 最热文章
     * @return
     */
    @PostMapping("hot")
    @Cache(expire = 5 * 60 * 1000,name = "hot_article")
    public Result hotArticle(){
        int limit =5;
        return articleService.hotArticles(limit);
    }

    /**
     *  首页 最新文章
     * @return
     */
    @PostMapping("new")
    @Cache(expire = 5 * 60 * 1000,name = "news_article")
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
    @Cache(expire = 5 * 60 * 1000,name = "view_article")
    public Result findArticleById(@PathVariable("id") Long articleId, @RequestBody ArticleViewParam articleViewParam){
        return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

    @PostMapping("{id}")
    public Result articleById(@PathVariable("id") Long articleId){
        return articleService.findArticleById(articleId);
    }

}
