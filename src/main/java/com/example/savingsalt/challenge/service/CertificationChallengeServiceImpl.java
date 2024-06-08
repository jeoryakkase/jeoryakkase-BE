package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
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
    private final CertificationChallengeImageServiceImpl certificationChallengeImageService;

    public CertificationChallengeServiceImpl(
        CertificationChallengeRepository certificationChallengeRepository,
        CertificationChallengeMapper certificationChallengeMapper,
        CertificationChallengeImageServiceImpl certificationChallengeImageService) {

        this.certificationChallengeRepository = certificationChallengeRepository;
        this.certificationChallengeMapper = certificationChallengeMapper;
        this.certificationChallengeImageService = certificationChallengeImageService;
    }

    // 회원 챌린지 일일 인증 생성
    public CertificationChallengeDto createCertificationChallenge(MemberChallengeEntity memberChallengeEntity,
        CertificationChallengeReqDto certificationChallengeReqDto) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        CertificationChallengeEntity certificationChallengeEntity = certificationChallengeMapper.toEntity(
            certificationChallengeReqDto);

        certificationChallengeEntity = certificationChallengeEntity.toBuilder()
            .memberChallengeEntity(memberChallengeEntity)
            .certificationDate(currentDateTime).build();

        certificationChallengeEntity = certificationChallengeRepository.save(
            certificationChallengeEntity);

        CertificationChallengeDto certificationChallengeDto = certificationChallengeMapper.toDto(
            certificationChallengeEntity);

        // 챌린지 인증 테이블에 이미지 저장
        certificationChallengeDto = certificationChallengeDto.toBuilder().certificationChallengeImageDtos(
                certificationChallengeImageService.createCertificationChallengeImage(
                    certificationChallengeReqDto.getImageUrls(), certificationChallengeEntity.getId()))
            .build();

        return certificationChallengeDto;
    }

}
