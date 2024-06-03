package com.example.savingsalt.community.comment.domain.dto;

import com.example.savingsalt.community.comment.domain.CommentEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import lombok.Getter;

@Getter
public class ReplyCommentReqDto {

    private String Comment;
    private CommentEntity parentComment;
    private MemberEntity member;


}
