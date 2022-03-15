package com.zzb.AutomaArticle.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzb.AutomaArticle.dao.mapper.CommentMapper;
import com.zzb.AutomaArticle.dao.pojo.Comment;
import com.zzb.AutomaArticle.dao.pojo.SysUser;
import com.zzb.AutomaArticle.service.CommentService;
import com.zzb.AutomaArticle.service.SysUserService;
import com.zzb.AutomaArticle.utils.UserThreadLocal;
import com.zzb.AutomaArticle.vo.CommentVO;
import com.zzb.AutomaArticle.vo.Result;
import com.zzb.AutomaArticle.vo.UserVO;
import com.zzb.AutomaArticle.vo.params.CommentParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public Result commentByArticleId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,id);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments = commentMapper.selectList(queryWrapper);
        List<CommentVO> CommentVOList = copyList(comments);
        return Result.success(CommentVOList);
    }

//    写评论
    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment = new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent = commentParam.getParent();
        if(parent == null || parent == 0){
            comment.setLevel(1);
        }else{
            comment.setLevel(2);
        }
        comment.setParentId(parent == null ? 0 : parent);
        Long toUserId = commentParam.getToUserId();
        comment.setToUid(toUserId == null ? 0 : toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);
    }

    private List<CommentVO> copyList(List<Comment> comments){
        List<CommentVO> CommentVOList = new ArrayList<>();
        for (Comment comment : comments) {
            CommentVOList.add(copy(comment));
        }
        return CommentVOList;
    }
    private CommentVO copy(Comment comment) {
        CommentVO CommentVO = new CommentVO();
        BeanUtils.copyProperties(comment,CommentVO);
        CommentVO.setId(String.valueOf(comment.getId()));
        //作者信息
        Long authorId = comment.getAuthorId();
        UserVO userVO = this.sysUserService.findUserVOById(authorId);
        CommentVO.setAuthor(userVO);
        //子评论
        Integer level = comment.getLevel();
        if (1 == level){
            Long id = comment.getId();
            List<CommentVO> CommentVOList = findCommentsByParentId(id);
            CommentVO.setChildrens(CommentVOList);
        }
        //to User 给谁评论
        if (level > 1){
            Long toUid = comment.getToUid();
            UserVO toUserVO = this.sysUserService.findUserVOById(toUid);
            CommentVO.setToUser(toUserVO);
        }
        return CommentVO;
    }

    private List<CommentVO> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        return copyList(commentMapper.selectList(queryWrapper));
    }

}
