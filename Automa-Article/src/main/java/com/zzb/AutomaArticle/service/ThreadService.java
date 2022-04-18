package com.zzb.AutomaArticle.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzb.AutomaArticle.dao.mapper.ArticleMapper;
import com.zzb.AutomaArticle.dao.pojo.Article;
import com.zzb.AutomaArticle.vo.ArticleCashMessage;
import com.zzb.AutomaArticle.vo.ArticleCashMessageId;
import com.zzb.AutomaArticle.vo.params.ArticleViewParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@Slf4j
public class ThreadService {

    @Resource
    private ArticleMapper articleMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @PostConstruct
    public void initViewCount(){
        List<Article> articles = articleMapper.selectList(new LambdaQueryWrapper<>());
        for (Article article : articles) {
            String viewCountStr = (String)redisTemplate.opsForHash().get("view_count", String.valueOf(article.getId()));
            if(viewCountStr == null){
                log.info("初始化view_count {} {}",article.getId(),article.getViewCounts());
                redisTemplate.opsForHash().put("view_count", String.valueOf(article.getId()),String.valueOf(article.getViewCounts()));
            }
        }
    }

    @Async("taskExecutor")
    public void updateArticleViewCount(Long articleId, ArticleViewParam articleViewParam, String redisKey){
        log.info("增加了view_count {}",articleId);
        redisTemplate.opsForHash().increment("view_count",String.valueOf(articleId),1);

        updateArticleList(articleViewParam);

    }

    private void updateArticleList( ArticleViewParam articleViewParam) {
        String key = JSON.toJSONString(articleViewParam);
        ArticleCashMessage articleCashMessage = new ArticleCashMessage();
        articleCashMessage.setRedisKey("listArticle::ArticleController::listArticle::"+key);
        articleCashMessage.setArticleViewParam(articleViewParam);

        rocketMQTemplate.asyncSend("automa-update-articleList", articleCashMessage, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                log.info("成功发送更新文章列表MQ");
            }

            @Override
            public void onException(Throwable throwable) {
                log.info("异常：发送更新文章列表MQ失败");
            }
        });
    }
}
