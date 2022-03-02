package com.zzb.AutomaArticle.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zzb.AutomaArticle.dao.dos.Archives;
import com.zzb.AutomaArticle.dao.pojo.Article;

import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {

    List<Archives> listArchives();
}
