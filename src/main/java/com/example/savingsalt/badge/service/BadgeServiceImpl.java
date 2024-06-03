package com.example.savingsalt.badge.service;

import com.example.savingsalt.badge.domain.dto.BadgeCreateReqDto;
import com.example.savingsalt.badge.domain.dto.BadgeDto;
import com.example.savingsalt.badge.domain.dto.MemberGoalBadgeDto;
import com.example.savingsalt.badge.domain.entity.BadgeEntity;
import com.example.savingsalt.badge.domain.dto.BadgeUpdateReqDto;
import com.example.savingsalt.badge.domain.dto.MemberChallengeBadgeResDto;
import com.example.savingsalt.badge.domain.entity.MemberGoalBadgeEntity;
import com.example.savingsalt.badge.domain.dto.MemberGoalBadgeResDto;
import com.example.savingsalt.badge.mapper.BadgeMainMapper;
import com.example.savingsalt.badge.repository.BadgeRepository;
import com.example.savingsalt.badge.repository.MemberGoalBadgeRepository;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.global.ChallengeException.BadgeNotFoundException;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BadgeServiceImpl implements BadgeService {

    private final BadgeRepository badgeRepository;
    private final MemberGoalBadgeRepository memberGoalBadgeRepository;
    private final MemberRepository memberRepository;
    private final MemberChallengeRepository memberChallengeRepository;
    private final BadgeMainMapper badgeMainMapper;

    public BadgeServiceImpl(BadgeRepository badgeRepository,
        MemberGoalBadgeRepository memberGoalBadgeRepository, MemberRepository memberRepository,
        MemberChallengeRepository memberChallengeRepository, BadgeMainMapper badgeMainMapper) {
        this.badgeRepository = badgeRepository;
        this.memberGoalBadgeRepository = memberGoalBadgeRepository;
        this.memberRepository = memberRepository;
        this.memberChallengeRepository = memberChallengeRepository;
        this.badgeMainMapper = badgeMainMapper;
    }

    // 해당 뱃지 조회
    @Transactional(readOnly = true)
    public BadgeEntity findById(Long badgeId) {
        return badgeRepository.findById(badgeId)
            .orElseThrow(BadgeNotFoundException::new);
    }

    // 모든 뱃지 정보 조회
    @Transactional(readOnly = true)
    public List<BadgeDto> getAllBadges() {
        List<BadgeEntity> allBadges = badgeRepository.findAll();
        if (allBadges.size() == 0) {
            return null;
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
        List<MemberGoalBadgeDto> memberGoalBadgesDto = badgeMainMapper.toMemberGoalBadgeDto(
            memberGoalBadges);

        List<BadgeEntity> badgeEntities = new ArrayList<>();
        for(int i=0;i<memberGoalBadgesDto.size();i++) {
            badgeEntities.add(memberGoalBadgesDto.get(i).getBadgeEntity());
        }

        List<MemberGoalBadgeResDto> memberGoalBadgesResDto = badgeMainMapper.toMemberGoalBadgeResDto(badgeEntities);

        return memberGoalBadgesResDto;
    }

    // 회원 챌린지 달성 뱃지 정보 조회
    @Transactional(readOnly = true)
    public List<MemberChallengeBadgeResDto> getMemberChallengeBadges(Long memberId) {
        MemberEntity member = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        List<MemberChallengeEntity> memberChallengeEntity = memberChallengeRepository.findAllByMemberEntity(
            member);
        if (memberChallengeEntity.size() == 0) {
            return null;
        }
        List<MemberChallengeBadgeResDto> memberChallengeBadgeResDto = new ArrayList<>();
        // 회원 챌린지에서 각각 처음 완료한 챌린지의 뱃지만 저장
        for (int i = 0; i < memberChallengeEntity.size(); i++) {
            if ((memberChallengeEntity.get(i).getChallengeStatus()
                == ChallengeStatus.COMPLETED) && (memberChallengeEntity.get(i).getChallengeTry()
                == 0)) {
                BadgeEntity badgeEntity = memberChallengeEntity.get(i).getChallengeEntity()
                    .getBadgeEntity();
                memberChallengeBadgeResDto.add(
                    badgeMainMapper.toMemberChallengeBadgeResDto(badgeEntity));
            }
        }

        return memberChallengeBadgeResDto;
    }

    // 뱃지 생성
    public BadgeDto createBadge(BadgeCreateReqDto badgeCreateReqDto) {
        BadgeEntity badgeEntity = badgeMainMapper.toEntity(badgeCreateReqDto);
        BadgeEntity createdBadge = badgeRepository.save(badgeEntity);

        BadgeDto createdBadgeDto = badgeMainMapper.toDto(createdBadge);

        return createdBadgeDto;
    }

    // 회원 목표 달성 뱃지 생성
    public BadgeDto createMemberGoalBadge(Long badgeId, Long memberId) {
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
            .orElseThrow(BadgeNotFoundException::new);
        MemberEntity memberEntity = memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        MemberGoalBadgeEntity memberGoalBadgeEntity = MemberGoalBadgeEntity.builder()
            .badgeEntity(badgeEntity)
            .memberEntity(memberEntity)
            .build();

        MemberGoalBadgeEntity createdMemberGoalBadgeEntity = memberGoalBadgeRepository.save(memberGoalBadgeEntity);
        BadgeDto createdMemberGoalBadgeDto = badgeMainMapper.toDto(createdMemberGoalBadgeEntity.getBadgeEntity());

        return createdMemberGoalBadgeDto;
    }

    // 뱃지 정보 수정
    public BadgeDto updateBadge(Long badgeId, BadgeUpdateReqDto badgeUpdateReqDto) {
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
            .orElseThrow(BadgeNotFoundException::new);
        BadgeEntity updateBadgeEntity = badgeEntity.toBuilder()
            .name(badgeUpdateReqDto.getName())
            .badgeDesc(badgeUpdateReqDto.getBadgeDesc())
            .badgeImage(badgeUpdateReqDto.getBadgeImage())
            .badgeType(badgeUpdateReqDto.getBadgeType())
            .build();

        BadgeEntity updatedBadge = badgeRepository.save(updateBadgeEntity);
        BadgeDto updateBadgeDto = badgeMainMapper.toDto(updatedBadge);

        return updateBadgeDto;
    }

    // 뱃지 삭제
    public void deleteBadge(Long badgeId) {
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
            .orElseThrow(BadgeNotFoundException::new);

        badgeRepository.deleteById(badgeId);
    }
}
