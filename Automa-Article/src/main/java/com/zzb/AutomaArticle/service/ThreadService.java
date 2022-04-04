package com.zzb.AutomaArticle.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzb.AutomaArticle.dao.mapper.ArticleMapper;
import com.zzb.AutomaArticle.dao.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
public class ThreadService {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void initViewCount(){
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<>());
        for (Article article : articles) {
            String viewCountStr = (String)redisTemplate.opsForHash().get("view_count", String.valueOf(article.getId()));
            if(viewCountStr == null){
                redisTemplate.opsForHash().put("view_count", String.valueOf(article.getId()),String.valueOf(article.getViewCounts()));
            }
        }
    }

    @Async("taskExecutor")
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article){
        redisTemplate.opsForHash().increment("view_count",String.valueOf(article.getId()),1);
    }
}
