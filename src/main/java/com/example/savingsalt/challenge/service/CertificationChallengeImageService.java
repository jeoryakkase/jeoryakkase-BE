package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeImageDto;
import java.util.List;

public interface CertificationChallengeImageService {
    List<CertificationChallengeImageDto> createCertificationChallengeImage(List<String> imageUrls, Long certificationChallengeId);
}
