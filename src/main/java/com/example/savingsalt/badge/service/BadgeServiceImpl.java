package com.example.savingsalt.badge.service;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.domain.dto.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.entity.MemberGoalBadgeEntity;
import com.example.savingsalt.badge.domain.dto.MemberGoalBadgeResDto;
import com.example.savingsalt.badge.mapper.BadgeMainMapper;
import com.example.savingsalt.badge.repository.BadgeRepository;
import com.example.savingsalt.badge.repository.MemberGoalBadgeRepository;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final MemberGoalBadgeRepository memberGoalBadgeRepository;
    private final MemberRepository memberRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final BadgeMainMapper badgeMainMapper;

    public BadgeServiceImpl(BadgeRepository badgeRepository,
        MemberGoalBadgeRepository memberGoalBadgeRepository, MemberRepository memberRepository, MemberChallengeRepository memberChallengeRepository, BadgeMainMapper badgeMainMapper) {
        this.badgeRepository = badgeRepository;
        this.memberGoalBadgeRepository = memberGoalBadgeRepository;
        this.memberRepository = memberRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.badgeMainMapper = badgeMainMapper;
    }

    // 모든 뱃지 정보 조회
    @Transactional(readOnly = true)
    public List<BadgeDto> getAllBadges() {
        List<BadgeEntity> allBadges = badgeRepository.findAll();
        if (allBadges.size() == 0) {
            // Todo: 예외발생 ("뱃지 정보들을 가져오는데 실패했습니다. or 생성된 벳지가 없습니다.");
        }
        List<BadgeDto> allBadgeResDto = badgeMainMapper.toDto(allBadges);

        return allBadgeResDto;
    }

    // 회원 목표 달성 뱃지 정보 조회
    @Transactional(readOnly = true)
    public List<MemberGoalBadgeResDto> getMemberGoalBadges(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        List<MemberGoalBadgeEntity> memberGoalBadges = memberGoalBadgeRepository.findALlByMemberEntity(
            member);
        List<MemberGoalBadgeResDto> memberGoalBadgesResDto = badgeMainMapper.toMemberGoalBadgeResDto(memberGoalBadges);

        return memberGoalBadgesResDto;
    }

    // 회원 챌린지 달성 뱃지 정보 조회
    @Transactional(readOnly = true)
    public List<MemberChallengeBadgeResDto> getMemberChallengeBadges(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        List<MemberChallengeEntity> memberChallengeEntity = memberChallengeRepository.findAllByMemberEntity(member);
        if (memberChallengeEntity.size() == 0) {
            // Todo: 예외발생 ("회원 챌린지 정보들을 가져오는데 실패했습니다. or 회원 챌린지 달성 뱃지가 없습니다.");
        }
        List<MemberChallengeBadgeResDto> memberChallengeBadgeResDto = new ArrayList<>();
        // 회원 챌린지가 완료된 뱃지만 저장
        for (int i = 0; i < memberChallengeEntity.size(); i++) {
            if(memberChallengeEntity.get(i).getChallengeStatus() == MemberChallengeEntity.ChallengeStatus.COMPLETED) {
                BadgeEntity badgeEntity = memberChallengeEntity.get(i).getChallengeEntity().getBadgeEntity();
                memberChallengeBadgeResDto.add(badgeMainMapper.toMemberChallengeBadgeResDto(badgeEntity));
            }
        }

        return memberChallengeBadgeResDto;
    }

    // 뱃지 생성
    @Transactional
    public BadgeDto createBadge(BadgeCreateReqDto badgeCreateReqDto) {
        BadgeEntity badgeEntity = badgeMainMapper.toEntity(badgeCreateReqDto);
        BadgeEntity createdBadge = badgeRepository.save(badgeEntity);
        // Todo: createdBadge가 null이면 예외발생 ("뱃지 정보를 저장하는데 실패했습니다.");
        BadgeDto createdBadgeDto = badgeMainMapper.toDto(createdBadge);

        return createdBadgeDto;
    }

    // 뱃지 정보 수정
    @Transactional
    public BadgeDto updateBadge(Long badgeId, BadgeUpdateReqDto badgeUpdateReqDto) {
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
            .orElseThrow(() -> new IllegalArgumentException("벳지를 찾을 수 없습니다."));
        badgeEntity.toBuilder()
            .name(badgeUpdateReqDto.getName())
            .badgeDesc(badgeUpdateReqDto.getBadgeDesc())
            .badgeImage(badgeUpdateReqDto.getBadgeImage())
            .badgeType(badgeUpdateReqDto.getBadgeType())
            .build();
        BadgeDto updateBadgeDto = badgeMainMapper.toDto(badgeEntity);

        return updateBadgeDto;
    }

    // 뱃지 삭제
    @Transactional
    public void deleteBadge(Long badgeId) {
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
            .orElseThrow(() -> new IllegalArgumentException("벳지를 찾을 수 없습니다."));
        badgeRepository.deleteById(badgeEntity.getId());
    }
}
