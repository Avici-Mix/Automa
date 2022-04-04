package com.zzb.AutomaArticle.service.mq;

import com.alibaba.fastjson.JSON;
import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.ArticleMessage;
import com.zzb.AutomaArticle.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Component
@RocketMQMessageListener(topic = "automa-update-article",consumerGroup = "automa-update-article-group")
public class ArticleListener implements RocketMQListener<ArticleMessage> {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Override
    public void onMessage(ArticleMessage articleMessage) {

        log.info("MQ收到消息:{}",articleMessage);
        //1、 更新查看文章详情的缓存
        Long articleId = articleMessage.getArticleId();
        String params = DigestUtils.md5Hex(articleId.toString());
        String redisKey = "view_article::ArticleController::findArticleById::"+params;
        Result articleResult = articleService.findArticleById(articleId);
        redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(articleResult), Duration.ofMillis(5 * 60 * 1000));
        log.info("更新了缓存:{}",redisKey);
        //2. 文章列表的缓存 不知道参数,解决办法 直接删除缓存
        Set<String> keys = redisTemplate.keys("listArticle*");
        keys.forEach(s -> {
            redisTemplate.delete(s);
            log.info("删除了文章列表的缓存:{}",s);
        });

    }
}
