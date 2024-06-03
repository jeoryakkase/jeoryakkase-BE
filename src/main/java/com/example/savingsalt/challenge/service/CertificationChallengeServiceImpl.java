package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertificationChallengeMapper;
import com.example.savingsalt.challenge.repository.CertificationChallengeRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CertificationChallengeServiceImpl {

    private final CertificationChallengeRepository certificationChallengeRepository;
    private final CertificationChallengeMapper certificationChallengeMapper;

    public CertificationChallengeServiceImpl(
        CertificationChallengeRepository certificationChallengeRepository
        , CertificationChallengeMapper certificationChallengeMapper) {

        this.certificationChallengeRepository = certificationChallengeRepository;
        this.certificationChallengeMapper = certificationChallengeMapper;
    }

    // 회원 챌린지 일일 인증 생성
    public void createCertificationChallenge(MemberChallengeEntity memberChallengeEntity,
        CertificationChallengeReqDto certificationChallengeReqDto) {

            LocalDateTime currentDateTime = LocalDateTime.now();

            CertificationChallengeEntity certificationChallengeEntity = certificationChallengeMapper.toEntity(
                certificationChallengeReqDto);

            certificationChallengeEntity.toBuilder()
                .memberChallengeEntity(memberChallengeEntity)
                .certificationDate(currentDateTime);

            certificationChallengeRepository.save(certificationChallengeEntity);
    }

}
