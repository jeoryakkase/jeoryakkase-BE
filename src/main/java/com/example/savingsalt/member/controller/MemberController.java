package com.example.savingsalt.member.controller;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.LoginRequestDto;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.service.MemberService;
import com.example.savingsalt.member.service.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/api/signup")
    @Operation(summary = "signup", description = "Signup new member")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Signup success",
            content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = MemberEntity.class))}),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "Member already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
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

    @PostMapping("/api/login")
    @Operation(summary = "login", description = "Member login")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Login success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        try {
            TokenResponseDto tokenResponseDto = memberService.login(dto);
            return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login failed");
        }
    }

    @PostMapping("/api/logout")
    @Operation(summary = "logout", description = "Member logout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
            }

            String token = authHeader.substring(7);
            Long expiration = tokenProvider.getExpiration(token);
            tokenBlacklistService.blacklistToken(token, expiration);
            return ResponseEntity.status(HttpStatus.OK).body("Logout success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Logout failed");
        }
    }
}
