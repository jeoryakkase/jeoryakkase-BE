package com.example.savingsalt.member.controller;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.service.LoginService;
import com.example.savingsalt.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @PostMapping("/api/signup")
    @Operation(summary = "회원 가입", description = "새로운 회원을 가입시킵니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "회원 가입 성공",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))}),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 회원"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> signup(@RequestBody SignupRequestDto dto) {
        try {
            MemberEntity memberEntity = memberService.signUp(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(memberEntity);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if ("이미 회원가입 한 이메일입니다.".equals(errorMessage) || "이미 존재하는 닉네임입니다.".equals(
                errorMessage)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }
    }

    @PostMapping("/api/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 합니다.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                logger.info("User logout success");
                logoutHandler.logout(request, response, auth);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
        }
    }
}
