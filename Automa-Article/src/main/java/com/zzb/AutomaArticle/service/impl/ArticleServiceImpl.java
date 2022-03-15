package com.zzb.AutomaArticle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzb.AutomaArticle.dao.dos.Archives;
import com.zzb.AutomaArticle.dao.mapper.ArticleBodyMapper;
import com.zzb.AutomaArticle.dao.mapper.ArticleMapper;
import com.zzb.AutomaArticle.dao.mapper.ArticleTagMapper;
import com.zzb.AutomaArticle.dao.pojo.Article;
import com.zzb.AutomaArticle.dao.pojo.ArticleBody;
import com.zzb.AutomaArticle.dao.pojo.ArticleTag;
import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.service.*;
import com.zzb.AutomaArticle.utils.UserThreadLocal;
import com.zzb.AutomaArticle.vo.*;
import com.zzb.AutomaArticle.vo.params.ArticleParam;
import com.zzb.AutomaArticle.vo.params.PageParamsVO;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TagService tagService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ThreadService threadService;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public Result listArticle(PageParamsVO pageParams) {
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
        List<ArticleVO> articleVOList = copyList(records,true,true);
        return Result.success(articleVOList) ;
    }

    @Override
    public Result hotArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
//        select id,title from article order by view_counts desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
//        select id,title from article order by create_date desc limit 5
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(copyList(articles,false,false));
    }

    @Override
    public Result listArchives() {
        List<Archives> archivesList = articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Override
    public Result findArticleById(Long articleId) {
        Article article = this.articleMapper.selectById(articleId);
        ArticleVO articleVO = copy(article, true, true,true,true);
        return Result.success(articleVO);
    }

    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();

        Article article;
        boolean isEdit = false;
        if(articleParam.getId()!=null){
//            编辑
            article = new Article();
            article.setId(articleParam.getId());
            article.setTitle(articleParam.getTitle());
            article.setSummary(articleParam.getSummary());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            articleMapper.updateById(article);
            isEdit = true;
        }else{
//            新建
            article = new Article();
            article.setAuthorId(sysUser.getId());
            article.setWeight(Article.Article_Common);
            article.setViewCounts(0);
            article.setTitle(articleParam.getTitle());
            article.setCommentCounts(0);
            article.setCreateDate(System.currentTimeMillis());
            article.setCategoryId(Long.parseLong(articleParam.getCategory().getId()));
            //插入之后 会生成一个文章id
            this.articleMapper.insert(article);
        }
//    tags
        List<TagVO> tags = articleParam.getTags();
        if(tags!=null){
            for (TagVO tag : tags) {

                Long articleId = article.getId();
                if(isEdit){
                    LambdaQueryWrapper<ArticleTag> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(ArticleTag::getArticleId,articleId);
                    articleTagMapper.delete(queryWrapper);
                }
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(Long.parseLong(tag.getId()));
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }
        }
//body
        if(isEdit){
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            LambdaQueryWrapper<ArticleBody> updateWrapper = Wrappers.lambdaQuery();
            updateWrapper.eq(ArticleBody::getArticleId,article.getId());
            articleBodyMapper.update(articleBody,updateWrapper);
        }else{
            ArticleBody articleBody = new ArticleBody();
            articleBody.setArticleId(article.getId());
            articleBody.setContent(articleParam.getBody().getContent());
            articleBody.setContentHtml(articleParam.getBody().getContentHtml());
            articleBodyMapper.insert(articleBody);

            article.setBodyId(articleBody.getId());
            articleMapper.updateById(article);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("id",article.getId().toString());

//        if (isEdit){
//            //发送一条消息给rocketmq 当前文章更新了，更新一下缓存吧
//            ArticleMessage articleMessage = new ArticleMessage();
//            articleMessage.setArticleId(article.getId());
//            rocketMQTemplate.convertAndSend("blog-update-article",articleMessage);
//        }


        return Result.success(map);
    }

    private List<ArticleVO> copyList(List<Article> records,boolean isTag,boolean isAuthor) {
        ArrayList<ArticleVO> articleVOList = new ArrayList<>();
        for (Article record : records) {
            articleVOList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVOList;
    }

    private ArticleVO copy(Article article,boolean isTag,boolean isAuthor, boolean isBody,boolean isCategory){
        ArticleVO articleVO = new ArticleVO();
        articleVO.setId(String.valueOf(article.getId()));
        BeanUtils.copyProperties(article,articleVO);
        articleVO.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        if(isTag){
            Long articleId = article.getId();
            articleVO.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(isAuthor){
            Long authorId = article.getAuthorId();
            articleVO.setAuthor(sysUserService.findUserById(authorId).getNickname());

        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVO.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVO.setCategory(categoryService.findCategoryById(categoryId));
        }
        return articleVO;
    }


    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
