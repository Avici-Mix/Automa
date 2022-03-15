package com.zzb.AutomaArticle.service;

import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.CommentParam;

public interface CommentService {
    Result commentByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
