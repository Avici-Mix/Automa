package com.zzb.AutomaArticle.vo;


import lombok.Data;

import java.util.List;

@Data
public class ArticleVO {
    private String id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer likeCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

//    private ArticleBodyVo body;

    private List<TagVO> tags;

//    private CategoryVo category;
}
