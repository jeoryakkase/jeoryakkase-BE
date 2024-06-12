package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeDto;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertifiCationChallengeMapper;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertificationChallengeImageMapper;
import com.example.savingsalt.challenge.repository.CertificationChallengeRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CertificationChallengeServiceImpl implements CertificationChallengeService {

    private final CertificationChallengeRepository certificationChallengeRepository;
    private final CertifiCationChallengeMapper certificationChallengeMapper;
    private final CertificationChallengeImageServiceImpl certificationChallengeImageService;
    private final CertificationChallengeImageMapper certificationChallengeImageMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    public CertificationChallengeServiceImpl(
        CertificationChallengeRepository certificationChallengeRepository,
        CertifiCationChallengeMapper certificationChallengeMapper,
        CertificationChallengeImageServiceImpl certificationChallengeImageService,
        CertificationChallengeImageMapper certificationChallengeImageMapper,
        EntityManager entityManager) {

        this.certificationChallengeRepository = certificationChallengeRepository;
        this.certificationChallengeMapper = certificationChallengeMapper;
        this.certificationChallengeImageService = certificationChallengeImageService;
        this.certificationChallengeImageMapper = certificationChallengeImageMapper;
        this.entityManager = entityManager;
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
        MemberEntity memberEntity = memberChallengeEntity.getMemberEntity();

        String nickname = memberEntity.getNickname();
        String profileImage = memberEntity.getProfileImage();
        Long representativeBadgeId = memberEntity.getRepresentativeBadgeId();
        Long challengeId = memberChallengeEntity.getChallengeEntity().getId();

        CertificationChallengeDto createCertificationChallengeDto = CertificationChallengeDto.builder()
            .content(certificationChallengeReqDto.getContent())
            .saveMoney(certificationChallengeReqDto.getSaveMoney())
            .nickname(nickname)
            .profileImage(profileImage)
            .representativeBadgeId(representativeBadgeId)
            .certificationDate(currentDateTime)
            .challengeId(challengeId)
            .build();

        CertificationChallengeEntity certificationChallengeEntity =
            certificationChallengeMapper.toEntity(createCertificationChallengeDto);

        certificationChallengeEntity = certificationChallengeEntity.toBuilder()
            .memberChallengeEntity(memberChallengeEntity)
            .build();

        // 챌린지 인증 이미지 컬럼 생성
        createCertificationChallengeDto = createCertificationChallengeDto.toBuilder()
            .id(certificationChallengeRepository.save(
            certificationChallengeEntity).getId())
            .certificationChallengeImageDtos(
                certificationChallengeImageService.createCertificationChallengeImage(
                    imageUrls,
                    certificationChallengeEntity.getId())).build();

        return createCertificationChallengeDto;
    }

    // 챌린지 인증 삭제
    public void deleteCertificationChallengeById(Long certificationChallengeId) {
        entityManager.clear();
        certificationChallengeRepository.deleteById(certificationChallengeId);
    }
}
