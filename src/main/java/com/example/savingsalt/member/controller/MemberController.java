package com.example.savingsalt.member.controller;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.member.domain.LoginRequestDto;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.MemberUpdateRequestDto;
import com.example.savingsalt.member.domain.OAuth2SignupRequestDto;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.repository.MemberRepository;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        MemberEntity memberEntity = memberService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberEntity);
    }

    @PostMapping("/api/signup/additional-info")
    @Operation(summary = "additional information for OAuth2 signup", description = "Save additional information after new OAuth2 login")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Save success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> saveAdditionalInfo(@RequestBody OAuth2SignupRequestDto dto) {
        MemberEntity updatedMember = memberService.saveAdditionalInfo(dto);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
    }

    @PostMapping("/api/login")
    @Operation(summary = "login", description = "Member login")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Login success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        TokenResponseDto tokenResponseDto = memberService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(tokenResponseDto);
    }

    @PostMapping("/api/logout")
    @Operation(summary = "logout", description = "Member logout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token");
        }

        String token = authHeader.substring(7);
        Long expiration = tokenProvider.getExpiration(token);
        tokenBlacklistService.blacklistToken(token, expiration);
        return ResponseEntity.status(HttpStatus.OK).body("Logout success");
    }

    @PutMapping("/api/members/{memberId}")
    @Operation(summary = "member update", description = "Update member info")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> updateMember(@PathVariable Long memberId,
        @RequestBody MemberUpdateRequestDto dto) {
        MemberEntity memberEntity = memberService.updateMember(memberId, dto.getPassword(),
            dto.getNickname(), dto.getAge(),
            dto.getGender(), dto.getIncome(), dto.getSavingGoal(), dto.getProfileImage());

        return ResponseEntity.status(HttpStatus.OK).body(memberEntity);
    }
}
