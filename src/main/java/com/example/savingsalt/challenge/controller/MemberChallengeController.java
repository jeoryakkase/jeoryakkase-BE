package com.example.savingsalt.challenge.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.savingsalt.challenge.domain.dto.CertificationChallengeReqDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeAbandonResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeCreateResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeJoinResDto;
import com.example.savingsalt.challenge.domain.dto.MemberChallengeWithCertifyAndChallengeResDto;
import com.example.savingsalt.challenge.service.MemberChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "MemberChallenge", description = "MemberChallenge API")
public class MemberChallengeController {

    private final MemberChallengeService memberChallengeService;
    private final AmazonS3 amazonS3Client;

    // 회원 챌린지 목록 조회
    @Operation(summary = "회원 챌린지 목록 조회", description = "모든 회원 챌린지를 조회하는 API")
    @GetMapping("/members/{memberId}/challenges")
    public ResponseEntity<List<MemberChallengeWithCertifyAndChallengeResDto>> getAllMemberChallenges(
        @Parameter(description = "ID of the member") @PathVariable Long memberId) {
        List<MemberChallengeWithCertifyAndChallengeResDto> memberChallengeWithCertifyAndChallengeResDtos = memberChallengeService.getMemberChallenges(
            memberId);

        return memberChallengeWithCertifyAndChallengeResDtos.isEmpty() ? ResponseEntity.status(
            HttpStatus.NO_CONTENT).build()
            : ResponseEntity.ok(memberChallengeWithCertifyAndChallengeResDtos);
    }

    // 회원 챌린지 생성
    @Operation(summary = "회원 챌린지 생성", description = "회원 아이디, 챌린지 아이디를 이용해서 회원 챌린지 생성하는 API")
    @PostMapping("/members/{memberId}/challenges/{challengeId}")
    public ResponseEntity<MemberChallengeCreateResDto> createMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the challenge") @PathVariable Long challengeId) {

        MemberChallengeCreateResDto createdMemberChallengeCreateResDto = memberChallengeService.createMemberChallenge(
            memberId, challengeId);

        if (createdMemberChallengeCreateResDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdMemberChallengeCreateResDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 회원 챌린지 인증
    @Operation(summary = "회원 챌린지 인증", description = "챌린지 인증 컬럼 생성/회원 챌린지 상태 변화")
    @PostMapping(value = "/members/{memberId}/challenges/{memberChallengeId}/certify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberChallengeDto> certifyDailyMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId,
        @RequestPart CertificationChallengeReqDto certificationChallengeReqDto,
        @RequestPart("uploadFiles") List<MultipartFile> multipartFiles)
        throws IOException {

        List<String> imageUrls = new ArrayList<>();
        String timestamp = String.valueOf(System.currentTimeMillis());

        for (MultipartFile file : multipartFiles) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest;

            String uploadFileName = file.getOriginalFilename() + "/" + timestamp;

            putObjectRequest = new PutObjectRequest(
                "my.eliceproject.s3.bucket",
                uploadFileName,
                file.getInputStream(),
                objectMetadata
            );

            amazonS3Client.putObject(putObjectRequest);

            String imageUrl = String.format(
                "https://s3.ap-southeast-2.amazonaws.com/my.eliceproject.s3.bucket/"
                    + uploadFileName);

            imageUrls.add(imageUrl);
        }

        certificationChallengeReqDto = certificationChallengeReqDto.toBuilder()
            .imageUrls(imageUrls)
            .build();

        MemberChallengeDto memberChallengeDto = memberChallengeService.certifyDailyMemberChallenge(
            memberId, memberChallengeId,
            certificationChallengeReqDto);

        if (memberChallengeDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberChallengeDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 회원 챌린지 포기
    @Operation(summary = "회원 챌린지 포기", description = "회원 챌린지를 포기 상태로 바꾸는 API")
    @PutMapping("/members/{memberId}/challenges/{memberChallengeId}/abandon")
    public ResponseEntity<MemberChallengeAbandonResDto> abandonMemberChallenge(
        @Parameter(description = "ID of the member") @PathVariable Long memberId,
        @Parameter(description = "ID of the memberChallengeId") @PathVariable Long memberChallengeId) {

        MemberChallengeAbandonResDto memberChallengeAbandonResDto = memberChallengeService.abandonMemberChallenge(
            memberId, memberChallengeId);

        if (memberChallengeAbandonResDto != null) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(memberChallengeAbandonResDto);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 참여 중인 챌린지 목록 조회
    @Operation(summary = "참여 중인 회원 챌린지 목록 조회", description = "참여 중인 회원 챌린지의 정보를 리스트로 응답 받는 API")
    @GetMapping("/members/{memberId}/challenges/join")
    public ResponseEntity<List<MemberChallengeJoinResDto>> getjoinMemberChallenge(
        @PathVariable Long memberId) {
        List<MemberChallengeJoinResDto> memberChallengeJoinResDtos = memberChallengeService.getJoiningMemberChallenge(
            memberId);

        if (!memberChallengeJoinResDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(memberChallengeJoinResDtos);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
