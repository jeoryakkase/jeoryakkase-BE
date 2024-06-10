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
import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    private final JwtTokenProvider tokenProvider;
    private final MemberMapper memberMapper;
    private final MemberService memberService;

    // 회원 챌린지 목록 조회
    @Operation(summary = "회원 챌린지 목록 조회", description = "모든 회원 챌린지를 조회하는 API")
    @GetMapping("/members/challenges")
    public ResponseEntity<List<MemberChallengeWithCertifyAndChallengeResDto>> getAllMemberChallenges(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request) {

        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        List<MemberChallengeWithCertifyAndChallengeResDto> memberChallengeWithCertifyAndChallengeResDtos = memberChallengeService.getMemberChallenges(
            memberEntity.getId());

        return memberChallengeWithCertifyAndChallengeResDtos.isEmpty() ? ResponseEntity.status(
            HttpStatus.NO_CONTENT).build()
            : ResponseEntity.ok(memberChallengeWithCertifyAndChallengeResDtos);
    }

    // 회원 챌린지 생성
    @Operation(summary = "회원 챌린지 생성", description = "회원 아이디, 챌린지 아이디를 이용해서 회원 챌린지 생성하는 API")
    @PostMapping("/members/challenges/{challengeId}")
    public ResponseEntity<MemberChallengeCreateResDto> createMemberChallenge(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request,
        @Parameter(description = "ID of the challenge") @PathVariable Long challengeId) {

        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        MemberChallengeCreateResDto createdMemberChallengeCreateResDto = memberChallengeService.createMemberChallenge(
            memberEntity.getId(), challengeId);

        if (createdMemberChallengeCreateResDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdMemberChallengeCreateResDto);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 회원 챌린지 인증
    @Operation(summary = "회원 챌린지 인증", description = "챌린지 인증 컬럼 생성/회원 챌린지 상태 변화")
    @PostMapping(value = "/members/challenges/{challengeId}/certify", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberChallengeDto> certifyDailyMemberChallenge(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request,
        @Parameter(description = "ID of the challengeId") @PathVariable Long challengeId,
        @RequestPart CertificationChallengeReqDto certificationChallengeReqDto,
        @RequestPart("uploadFiles") List<MultipartFile> multipartFiles)
        throws IOException {

        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        List<String> imageUrls = new ArrayList<>();
        String timestamp = String.valueOf(System.currentTimeMillis());

        for (MultipartFile file : multipartFiles) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(file.getContentType());
            objectMetadata.setContentLength(file.getSize());

            PutObjectRequest putObjectRequest;

            String uploadFileName = file.getOriginalFilename() + "/" + timestamp;
            // test01.jpg/0043885293124
            // https://s3.ap-southeast-2.amazonaws.com/my.eliceproject.s3.bucket/test01.jpg/0043885293124

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

        MemberChallengeDto memberChallengeDto = memberChallengeService.certifyDailyMemberChallenge(
            memberEntity.getId(), challengeId,
            certificationChallengeReqDto, imageUrls);

        if (memberChallengeDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(memberChallengeDto);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 회원 챌린지 포기
    @Operation(summary = "회원 챌린지 포기", description = "회원 챌린지를 포기 상태로 바꾸는 API")
    @PutMapping("/members/challenges/{challengeId}/abandon")
    public ResponseEntity<MemberChallengeAbandonResDto> abandonMemberChallenge(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request,
        @Parameter(description = "ID of the challengeId") @PathVariable Long challengeId) {

        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        MemberChallengeAbandonResDto memberChallengeAbandonResDto = memberChallengeService.abandonMemberChallenge(
            memberEntity.getId(), challengeId);

        if (memberChallengeAbandonResDto != null) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(memberChallengeAbandonResDto);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 참여 중인 챌린지 목록 조회
    @Operation(summary = "참여 중인 회원 챌린지 목록 조회", description = "참여 중인 회원 챌린지의 정보를 리스트로 응답 받는 API")
    @GetMapping("/members/challenges/join")
    public ResponseEntity<List<MemberChallengeJoinResDto>> getjoinMemberChallenge(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request) {

        MemberEntity memberEntity = memberService.getMemberFromRequest(request);

        List<MemberChallengeJoinResDto> memberChallengeJoinResDtos = memberChallengeService.getJoiningMemberChallenge(
            memberEntity.getId());

        if (!memberChallengeJoinResDtos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(memberChallengeJoinResDtos);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    // 회원 챌린지 인증 삭제
    @Operation(summary = "회원 챌린지 인증 삭제", description = "챌린지 인증 컬럼 삭제와 챌린지 인증 시 업로드한 이미지 경로 정보들도 전부 삭제")
    @DeleteMapping(value = "/members/challenges/{challengeId}/certify/{certificationId}")
    public ResponseEntity<Void> certifyDailyMemberChallenge(
        @Parameter(description = "클라이언트의 요청 정보") HttpServletRequest request,
        @Parameter(description = "챌린지 ID") @PathVariable Long challengeId,
        @Parameter(description = "챌린지 인증 ID") @PathVariable Long certificationId) {

        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        memberChallengeService.deleteCertificationChallenge(memberEntity.getId(), challengeId, certificationId);

        return ResponseEntity.ok().build();
    }
}
