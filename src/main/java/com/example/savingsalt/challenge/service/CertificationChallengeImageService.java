package com.example.savingsalt.challenge.service;

import com.example.savingsalt.challenge.domain.dto.CertificationChallengeImageDto;
import java.util.List;

public interface CertificationChallengeImageService {
    CertificationChallengeImageDto createCertificationChallengeImage(List<String> imageUrl, Long certificationChallengeId);
}
