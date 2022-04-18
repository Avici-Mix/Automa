package com.zzb.AutomaArticle.service.mq;

import com.alibaba.fastjson.JSON;
import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.ArticleCashMessage;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.ArticleViewParam;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Set;

@Slf4j
@Component
@RocketMQMessageListener(topic = "automa-update-articleList",consumerGroup = "automa-update-articleList-group")
public class ArticleListListener implements RocketMQListener<ArticleCashMessage> {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Override
    public void onMessage(ArticleCashMessage message) {

        log.info("MQ收到消息:{}",message);
        ArticleViewParam articleViewParam = message.getArticleViewParam();
        PageParamsVO pageParamsVO = new PageParamsVO();
        try {
            BeanUtils.copyProperties(pageParamsVO,articleViewParam);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        Result articleResult = articleService.listArticle(pageParamsVO);
        redisTemplate.opsForValue().set(message.getRedisKey(), JSON.toJSONString(articleResult), Duration.ofMillis(5 * 60 * 1000));

//
        log.info("更新了文章列表的缓存:{}",message.getRedisKey());
    }
}

