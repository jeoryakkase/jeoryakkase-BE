package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertificationChallengeMapper;
import com.example.savingsalt.challenge.repository.CertificationChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CertificationChallengeServiceImpl {

    private final CertificationChallengeRepository certificationChallengeRepository;
    private final CertificationChallengeMapper certificationChallengeMapper;
    private final MemberChallengeRepository memberChallengeRepository;

    public CertificationChallengeServiceImpl(
        CertificationChallengeRepository certificationChallengeRepository
        , CertificationChallengeMapper certificationChallengeMapper
        , MemberChallengeRepository memberChallengeRepository) {

        this.certificationChallengeRepository = certificationChallengeRepository;
        this.certificationChallengeMapper = certificationChallengeMapper;
        this.memberChallengeRepository = memberChallengeRepository;
    }

    // 회원 챌린지 일일 인증 이미지 경로 및 인증 날짜 업로드
    public void uploadDailyChallengeImageUrlAndDateTime(
        Long memberChallngeId, CertificationChallengeDto certificationChallengeDto) {

        MemberChallengeEntity memberChallengeEntity = memberChallengeRepository.findById(
            memberChallngeId).orElse(null);

        CertificationChallengeEntity certificationChallengeEntity = certificationChallengeMapper.toEntity(
            certificationChallengeDto);

        certificationChallengeEntity.toBuilder().memberChallengeEntity(memberChallengeEntity);

        certificationChallengeRepository.save(certificationChallengeEntity);
    }

}
