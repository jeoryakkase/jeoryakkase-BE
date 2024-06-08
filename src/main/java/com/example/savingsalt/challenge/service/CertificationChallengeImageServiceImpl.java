package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeImageDto;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeEntity;
import com.example.savingsalt.challenge.domain.entity.CertificationChallengeImageEntity;
import com.example.savingsalt.challenge.exception.ChallengeException.CertificationChallengeNotFoundException;
import com.example.savingsalt.challenge.mapper.ChallengeMainMapper.CertificationChallengeImageMapper;
import com.example.savingsalt.challenge.repository.CertificationChallengeImageRepository;
import com.example.savingsalt.challenge.repository.CertificationChallengeRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CertificationChallengeImageServiceImpl {

    private final CertificationChallengeImageRepository certificationChallengeImageRepository;
    private final CertificationChallengeRepository certificationChallengeRepository;
    private final CertificationChallengeImageMapper certificationChallengeImageMapper;


    public CertificationChallengeImageServiceImpl(
        CertificationChallengeImageRepository certificationChallengeImageRepository,
        CertificationChallengeRepository certificationChallengeRepository,
        CertificationChallengeImageMapper certificationChallengeImageMapper) {
        this.certificationChallengeImageRepository = certificationChallengeImageRepository;
        this.certificationChallengeRepository = certificationChallengeRepository;
        this.certificationChallengeImageMapper = certificationChallengeImageMapper;
    }

    public List<CertificationChallengeImageDto> createCertificationChallengeImage(
        List<String> imageUrls, Long certificationChallengeId) {
        List<CertificationChallengeImageDto> certificationChallengeImageDtos = new ArrayList<>();

        Optional<CertificationChallengeEntity> certificationChallengeEntityOpt = certificationChallengeRepository.findById(
            certificationChallengeId);

        if (certificationChallengeEntityOpt.isPresent()) {
            CertificationChallengeEntity foundCertificationChallengeEntity = certificationChallengeEntityOpt.get();

            for (String imageUrl : imageUrls) {
                CertificationChallengeImageEntity certificationChallengeImageEntity = CertificationChallengeImageEntity.builder()
                    .imageUrl(imageUrl)
                    .certificationChallenge(foundCertificationChallengeEntity)
                    .build();

                certificationChallengeImageDtos.add(certificationChallengeImageMapper.toDto(
                    certificationChallengeImageRepository.save(certificationChallengeImageEntity)));
            }

            return certificationChallengeImageDtos;

        } else {
            throw new CertificationChallengeNotFoundException();
        }
    }
}
