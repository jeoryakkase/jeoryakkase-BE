package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertificationChallengeMapper;
import com.example.savingsalt.challenge.repository.CertificationChallengeRepository;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.global.ChallengeException.MemberChallengeNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
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

    // 회원 챌린지 일일 인증 생성
    public void createCertificationChallenge(Long memberChallngeId,
        CertificationChallengeReqDto certificationChallengeReqDto) {

        Optional<MemberChallengeEntity> memberChallengeEntityOpt = memberChallengeRepository.findById(
            memberChallngeId);

        if (memberChallengeEntityOpt.isPresent()) {
            LocalDateTime currentDateTime = LocalDateTime.now();

            CertificationChallengeEntity certificationChallengeEntity = certificationChallengeMapper.toEntity(
                certificationChallengeReqDto);

            certificationChallengeEntity.toBuilder()
                .memberChallengeEntity(memberChallengeEntityOpt.get())
                .certificationDate(currentDateTime);

            certificationChallengeRepository.save(certificationChallengeEntity);
        } else {
            throw new MemberChallengeNotFoundException();
        }
    }

}
