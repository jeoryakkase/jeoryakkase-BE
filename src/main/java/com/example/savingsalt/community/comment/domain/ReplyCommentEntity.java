package com.example.savingsalt.community.comment.domain;

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

@Table(name = "reply_comments")
@Getter
@Entity
public class ReplyCommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reply;

    private int depth;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    public void saveReply(CommentEntity parentComment, String reply, int depth) {
        this.parentComment = parentComment;
        this.reply = reply;
        this.depth = depth;
    }

}
