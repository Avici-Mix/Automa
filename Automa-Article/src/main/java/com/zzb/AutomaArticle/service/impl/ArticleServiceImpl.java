package com.zzb.AutomaArticle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzb.AutomaArticle.dao.mapper.ArticleMapper;
import com.zzb.AutomaArticle.dao.pojo.Article;
import com.zzb.AutomaArticle.service.ArticleService;
import com.zzb.AutomaArticle.vo.ArticleVO;
import com.zzb.AutomaArticle.vo.ResultVO;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Override
    public ResultVO listArticle(PageParamsVO pageParams) {
        /**
         * 分页查询数据库表
         * @param pageParams
         * @return
         */

        Page<Article> page = new Page<>(pageParams.getPage(), pageParams.getPageSize());
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //order by create data desc  先按照置顶排序，再按照时间排序
        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> records = articlePage.getRecords();
        List<ArticleVO> articleVOList = copyList(records);
        return ResultVO.success(articleVOList) ;
    }

    private List<ArticleVO> copyList(List<Article> records) {
        ArrayList<ArticleVO> articleVOList = new ArrayList<>();
        for (Article record : records) {
            articleVOList.add(copy(record));
        }
        return articleVOList;
    }

    private ArticleVO copy(Article article){
        ArticleVO articleVO = new ArticleVO();
        BeanUtils.copyProperties(article,articleVO);
        articleVO.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        return articleVO;
    }
}
