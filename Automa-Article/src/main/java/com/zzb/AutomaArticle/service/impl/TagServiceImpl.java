package com.zzb.AutomaArticle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzb.AutomaArticle.dao.mapper.TagMapper;
import com.zzb.AutomaArticle.dao.pojo.Tag;
import com.zzb.AutomaArticle.service.TagService;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.TagVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Override
    public Result hots(int limit) {
        /**
         * 标签拥有的文章量最多
         * 根据tag_id分组计数，从大到校排列
         */

        List<Long> tagIds = tagMapper.findHotsTagIds(limit);
       if(CollectionUtils.isEmpty(tagIds)) {
           return Result.success(Collections.emptyList());
       }
        List<Tag> tagList = tagMapper.findTagsByTagIds(tagIds);
        return Result.success(tagList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findAllDetail() {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        List<Tag> tags = this.tagMapper.selectList(queryWrapper);
        return Result.success(copyList(tags));
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagVO copy = copy(tag);
        return Result.success(copy);
    }

    @Override
    public List<TagVO> findTagsByArticleId(Long articleId) {
        List<Tag> tags = tagMapper.findTagsByArticleId(articleId);
        return copyList(tags);
        
    }

    public TagVO copy(Tag tag){
        TagVO tagVo = new TagVO();
        BeanUtils.copyProperties(tag,tagVo);
        tagVo.setId(String.valueOf(tag.getId()));
        return tagVo;
    }
    public List<TagVO> copyList(List<Tag> tagList){
        List<TagVO> TagVOList = new ArrayList<>();
        for (Tag tag : tagList) {
            TagVOList.add(copy(tag));
        }
        return TagVOList;
    }
}
