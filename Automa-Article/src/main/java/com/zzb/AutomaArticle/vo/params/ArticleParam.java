package com.zzb.AutomaArticle.vo.params;

import com.zzb.AutomaArticle.vo.CategoryVO;
import com.zzb.AutomaArticle.vo.TagVO;
import lombok.Data;

import java.util.List;

@Data
public class ArticleParam {

    private Long id;

    private ArticleBodyParam body;

    private CategoryVO category;

    private String summary;

    private List<TagVO> tags;

    private String title;
}
