package com.example.savingsalt.member.controller;

import com.example.savingsalt.member.domain.dto.LoginRequestDto;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.domain.dto.MemberUpdateRequestDto;
import com.example.savingsalt.member.domain.dto.MemberUpdateResponseDto;
import com.example.savingsalt.member.domain.dto.MyPageResponseDto;
import com.example.savingsalt.member.domain.dto.SignupRequestDto;
import com.example.savingsalt.member.exception.MemberException.EmailAlreadyExistsException;
import com.example.savingsalt.member.exception.MemberException.MemberNotFoundException;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberUpdateMapper;
import com.example.savingsalt.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Member", description = "회원 API")
public class MemberController {

    private final MemberService memberService;
    private final MemberMapper memberMapper;
    private final MemberUpdateMapper memberUpdateMapper;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원가입", description = "Signup new member")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Signup success",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "Member already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> signup(@RequestPart("dto") SignupRequestDto dto,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        MemberEntity memberEntity = memberService.signUp(dto, profileImage);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PostMapping("/signup/additional-info")
//    @Operation(summary = "additional information for OAuth2 signup", description = "Save additional information after new OAuth2 login")
//    @ApiResponses({
//        @ApiResponse(responseCode = "201", description = "Save success"),
//        @ApiResponse(responseCode = "400", description = "Bad request"),
//        @ApiResponse(responseCode = "500", description = "Server error")
//    })
//    public ResponseEntity<?> saveAdditionalInfo(HttpServletRequest request, @RequestBody OAuth2SignupRequestDto dto) {
//        String token = tokenProvider.resolveToken(request);
//        if (token == null || !tokenProvider.validateToken(token)) {
//            throw new MemberException.InvalidTokenException();
//        }
//
//        String email = tokenProvider.getEmailFromToken(token);
//
//        MemberEntity updatedMember = memberService.saveAdditionalInfo(email, dto);
//        return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
//    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "Member login")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginRequestDto dto) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid Authorization header");
        }

        String accessToken = authHeader.replace("Bearer ", "");
        String refreshToken = memberService.login(dto, accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("refreshToken", refreshToken);

        ResponseEntity<?> response = ResponseEntity.status(HttpStatus.CREATED)
            .headers(headers)
            .body(responseBody);

        return response;
    }

//    @PostMapping("/logout")
//    @Operation(summary = "로그아웃", description = "Member logout")
//    @ApiResponses({
//        @ApiResponse(responseCode = "200", description = "Logout success"),
//        @ApiResponse(responseCode = "400", description = "Bad request"),
//        @ApiResponse(responseCode = "500", description = "Server error")
//    })
//    public ResponseEntity<?> logout(HttpServletRequest request) {
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            throw new MemberException.InvalidTokenException();
//        }
//
//        String token = authHeader.substring(7);
//        Long expiration = tokenProvider.getExpiration(token);
//        tokenBlacklistService.blacklistToken(token, expiration);
//        return ResponseEntity.status(HttpStatus.OK).body("Logout success");
//    }

    @GetMapping("/members/update-info")
    @Operation(summary = "회원 정보 수정 페이지 조회", description = "Get member info page for update")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<MemberUpdateResponseDto> getMemberUpdatePage(HttpServletRequest request) {
        String email = memberService.getEmailFromRequest(request);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberNotFoundException("email", email);
        }

        MemberUpdateResponseDto responseDto = memberUpdateMapper.toDto(memberEntity);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/members/mypage")
    @Operation(summary = "회원 마이페이지 조회", description = "Get member mypage")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<MyPageResponseDto> getMyPage(HttpServletRequest request) {
        String email = memberService.getEmailFromRequest(request);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberNotFoundException("email", email);
        }

        MyPageResponseDto responseDto = memberService.getMyPage(memberEntity.getId());
        return ResponseEntity.ok(responseDto);
    }


    @PatchMapping(value = "/members/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "회원 정보 수정", description = "Update member info")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> updateMember(HttpServletRequest request,
        @RequestPart("dto") MemberUpdateRequestDto dto,
        @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) throws IOException {
        String email = memberService.getEmailFromRequest(request);
        Long memberId = memberService.findMemberByEmail(email).getId();

        MemberEntity memberEntity = memberService.updateMember(memberId, dto.getEmail(),
            dto.getPassword(),
            dto.getNickname(), dto.getAge(),
            dto.getGender(), dto.getIncome(), dto.getSavePurpose(), profileImage,
            dto.getInterests(), dto.getAbout());

        return ResponseEntity.status(HttpStatus.OK).body(memberEntity);
    }

    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복 검사", description = "Check whether this email already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "This email already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> checkEmail(@RequestParam(value = "email") String email) {
        boolean isAvailable = memberService.checkEmail(email);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isAvailable", isAvailable);

        // 이메일이 이미 존재할 경우
        if (!isAvailable) {
            responseBody.put("errorCode", "DUPLICATED_ERROR");
            responseBody.put("errorMessage", "This email already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "닉네임 중복 검사", description = "Check whether this nickname already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "This nickname already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> checkNickname(@RequestParam(value = "nickname") String nickname) {
        boolean isAvailable = memberService.checkNickname(nickname);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isAvailable", isAvailable);

        // 닉네임이 이미 존재할 경우
        if (!isAvailable) {
            responseBody.put("errorCode", "DUPLICATED_ERROR");
            responseBody.put("errorMessage", "This nickname already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }

        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping("members/signout")
    @Operation(summary = "회원 탈퇴", description = "Member signout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Signout success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized member"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Void> signout(HttpServletRequest request) {
        String email = memberService.getEmailFromRequest(request);
        Long memberId = memberService.findMemberByEmail(email).getId();

        memberService.signOut(memberId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/admin/members/{memberId}")
    @Operation(summary = "관리자의 회원 삭제", description = "Delete member")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Delete success"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> deleteMember(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body("Delete success");
    }
}
