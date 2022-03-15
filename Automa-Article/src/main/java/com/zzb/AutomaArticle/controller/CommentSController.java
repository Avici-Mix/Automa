package com.zzb.AutomaArticle.controller;

import com.zzb.AutomaArticle.service.CommentService;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.params.CommentParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("comments")
public class CommentSController {

    @Autowired
    private CommentService commentService;

    @GetMapping("article/{id}")
    public Result comments(@PathVariable("id") Long id){
        return commentService.commentByArticleId(id);
    }

    @PostMapping("create/change")
    public Result comment(@RequestBody CommentParam commentParam){
        return commentService.comment(commentParam);
    }
}
