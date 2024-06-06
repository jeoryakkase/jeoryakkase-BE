package com.example.savingsalt.community.comment.domain;

import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "reply_comments")
@Getter
@NoArgsConstructor
@Entity
public class ReplyCommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;


    public ReplyCommentEntity(String comment, CommentEntity parentComment, MemberEntity memberEntity) {
        this.content = content;
        this.parentComment = parentComment;
        this.memberEntity = memberEntity;
    }

    public void update(ReplyCommentReqDto requestDto) {
        this.content = requestDto.getContent();
    }

}
