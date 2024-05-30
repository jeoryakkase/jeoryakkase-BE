package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.MemberChallengeEntity;
import com.example.savingsalt.challenge.domain.MemberChallengeEntity.ChallengeStatus;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.MemberChallengeMapper;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MemberChallengeServiceImpl implements
    MemberChallengeService {

    private final MemberChallengeRepository memberChallengeRepository;
    private final MemberChallengeMapper memberChallengeMapper;
    private final MemberRepository memberRepository;

    public MemberChallengeServiceImpl(MemberChallengeRepository memberChallengeRepository,
        MemberChallengeMapper memberChallengeMapper
        , MemberRepository memberRepository) {

        this.memberChallengeRepository = memberChallengeRepository;
        this.memberChallengeMapper = memberChallengeMapper;
        this.memberRepository = memberRepository;
    }

    // 회원 챌린지 목록 조회
    public List<MemberChallengeDto> getMemberChallenges(Long memberId) {

        MemberEntity foundMember = memberRepository.findById(memberId).orElse(null);
        return memberChallengeMapper.toDto(
            memberChallengeRepository.findAllByMemberEntity(foundMember));
    }

    // 회원 챌린지 생성
    public MemberChallengeDto createMemberChallenge(
        MemberChallengeDto memberChallengeDto) {

        return memberChallengeMapper.toDto(
            memberChallengeRepository.save(memberChallengeMapper.toEntity(memberChallengeDto)));
    }

    // 회원 챌린지 완료
    public void authenticateFinalChallenge(Long memberId, Long memberChallengeId) {
        MemberEntity foundMemberEntity = memberRepository.findById(memberId).orElseThrow(
            NoSuchElementException::new);
        MemberChallengeEntity foundMemberChallengeEntity = null;

        List<MemberChallengeEntity> memberChallengeEntities = foundMemberEntity.getMemberChallengeEntities();

        for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
            if (memberChallengeEntity.getId().equals(memberChallengeId)) {
                foundMemberChallengeEntity = memberChallengeEntity;
            }
        }

        LocalDateTime currentDate = LocalDateTime.now();

        Objects.requireNonNull(foundMemberChallengeEntity).toBuilder()
            .challengeStatus(ChallengeStatus.COMPLETED)
            .certifyDate(currentDate)
            .build();

        memberChallengeRepository.save(foundMemberChallengeEntity);
    }

    // 회원 챌린지 포기
    public void abandonMemberChallenge(Long memberChallengeId) {
        MemberChallengeEntity foundMemberchallenge = memberChallengeRepository.findById(
            memberChallengeId).orElse(null);

        Objects.requireNonNull(foundMemberchallenge).toBuilder()
            .challengeStatus(ChallengeStatus.CANCELLED)
            .build();

        memberChallengeRepository.save(foundMemberchallenge);
    }

}
