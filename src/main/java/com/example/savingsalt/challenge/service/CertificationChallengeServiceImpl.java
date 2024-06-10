package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertifiCationChallengeMapper;
import com.example.savingsalt.challenge.repository.CertificationChallengeRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CertificationChallengeServiceImpl {

    private final CertificationChallengeRepository certificationChallengeRepository;
    private final CertifiCationChallengeMapper certificationChallengeMapper;
    private final CertificationChallengeImageServiceImpl certificationChallengeImageService;

    public CertificationChallengeServiceImpl(
        CertificationChallengeRepository certificationChallengeRepository,
        CertifiCationChallengeMapper certificationChallengeMapper,
        CertificationChallengeImageServiceImpl certificationChallengeImageService) {

        this.certificationChallengeRepository = certificationChallengeRepository;
        this.certificationChallengeMapper = certificationChallengeMapper;
        this.certificationChallengeImageService = certificationChallengeImageService;
    }

    // 챌린지 인증 내용 조회
    public CertificationChallengeDto getCertifiCationChallenge(
        MemberChallengeEntity memberChallengeEntity) {

        return certificationChallengeMapper.toDto(
            certificationChallengeRepository.findByMemberChallengeEntity(memberChallengeEntity));
    }

    // 챌린지 인증 컬럼 생성
    public CertificationChallengeDto createCertificationChallenge(
        MemberChallengeEntity memberChallengeEntity,
        CertificationChallengeReqDto certificationChallengeReqDto, List<String> imageUrls) {

        LocalDateTime currentDateTime = LocalDateTime.now();

        CertificationChallengeEntity certificationChallengeEntity =
            certificationChallengeMapper.certificationChallengeDtoToCertificationChallengeEntity(
            certificationChallengeReqDto);

        certificationChallengeEntity = certificationChallengeEntity.toBuilder()
            .memberChallengeEntity(memberChallengeEntity)
            .certificationDate(currentDateTime).build();

        certificationChallengeEntity = certificationChallengeRepository.save(
            certificationChallengeEntity);

        CertificationChallengeDto certificationChallengeDto = certificationChallengeMapper.toDto(
            certificationChallengeEntity);

        // 챌린지 인증 이미지 컬럼 생성
        certificationChallengeDto = certificationChallengeDto.toBuilder()
            .certificationChallengeImageDtos(
                certificationChallengeImageService.createCertificationChallengeImage(
                    imageUrls,
                    certificationChallengeEntity.getId()))
            .build();

        return certificationChallengeDto;
    }

    // 챌린지 인증 삭제
    public void deleteCertificationChallengeById(Long certificationChallengeId) {
        certificationChallengeRepository.deleteById(certificationChallengeId);
    }
}
