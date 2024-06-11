package com.example.savingsalt.member.domain.dto;

import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.community.board.domain.dto.MyPageBoardDto;
import com.example.savingsalt.community.board.domain.entity.BoardEntity;
import com.example.savingsalt.community.bookmark.domain.BookmarkEntity;
import com.example.savingsalt.goal.domain.dto.GoalResponseDto;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponseDto {

    private Long memberId;
    private String nickname;
    private String profileImage;
    private String about;
    private BadgeDto representativeBadge;
    private List<MemberChallengeWithCertifyAndChallengeResDto> memberChallenges;
    private List<GoalResponseDto> goals;
    private List<MyPageBoardDto> bookmarks;
}
