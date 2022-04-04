package com.zzb.AutomaArticle.service.mq;

import com.zzb.AutomaArticle.vo.ArticleMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RocketMQMessageListener(topic = "automa-update-articleList",consumerGroup = "automa-update-articleList-group")
public class ArticleListListener implements RocketMQListener<ArticleMessage> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onMessage(ArticleMessage o) {
        Set<String> keys = redisTemplate.keys("listArticle*");
        keys.forEach(s -> {
            redisTemplate.delete(s);
            log.info("删除了文章列表的缓存:{}",s);
        });
    }
}

