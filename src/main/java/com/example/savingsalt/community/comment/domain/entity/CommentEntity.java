package com.example.savingsalt.community.comment.domain.entity;

import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.comment.domain.dto.CommentReqDto;
import com.example.savingsalt.community.comment.domain.dto.ReplyCommentReqDto;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "comments")
@Getter
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComment;

    private int depth;

    private Long level;

    public CommentEntity(CommentReqDto requestDto, BoardEntity saveBoard, MemberEntity member, CommentEntity parentComent, int depth, Long level) {
        this.content = requestDto.getContent();
        this.boardEntity = saveBoard;
        this.memberEntity = member;
        this.parentComment = parentComent;
        this.depth = depth;
        this.level = level;
    }

    public void updateCotent(String content) {
        this.content = content;
    }

    public void updateReply(ReplyCommentReqDto requestDto) {
        this.content = requestDto.getContent();
    }


}
