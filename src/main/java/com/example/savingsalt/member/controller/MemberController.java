package com.example.savingsalt.member.controller;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.global.UnauthorizedException;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Member", description = "Member API")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider tokenProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/signup")
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
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @PostMapping("/signup/additional-info")
//    @Operation(summary = "additional information for OAuth2 signup", description = "Save additional information after new OAuth2 login")
//    @ApiResponses({
//        @ApiResponse(responseCode = "201", description = "Save success"),
//        @ApiResponse(responseCode = "400", description = "Bad request"),
//        @ApiResponse(responseCode = "500", description = "Server error")
//    })
//    public ResponseEntity<?> saveAdditionalInfo(@RequestBody OAuth2SignupRequestDto dto) {
//        MemberEntity updatedMember = memberService.saveAdditionalInfo(dto);
//        return ResponseEntity.status(HttpStatus.OK).body(updatedMember);
//    }

    @PostMapping("/login")
    @Operation(summary = "login", description = "Member login")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Login success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        String email = memberService.login(dto);
        return ResponseEntity.status(HttpStatus.OK).body(email + " login success");
    }

    @PostMapping("/logout")
    @Operation(summary = "logout", description = "Member logout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MemberException.InvalidTokenException();
        }

        String token = authHeader.substring(7);
        Long expiration = tokenProvider.getExpiration(token);
        tokenBlacklistService.blacklistToken(token, expiration);
        return ResponseEntity.status(HttpStatus.OK).body("Logout success");
    }

    @PatchMapping("/members/{memberId}")
    @Operation(summary = "member update", description = "Update member info")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> updateMember(@PathVariable Long memberId,
        @RequestBody MemberUpdateRequestDto dto) {
        MemberEntity memberEntity = memberService.updateMember(memberId, dto.getEmail(), dto.getPassword(),
            dto.getNickname(), dto.getAge(),
            dto.getGender(), dto.getIncome(), dto.getSavePurpose(), dto.getProfileImage(), dto.getInterests());

        return ResponseEntity.status(HttpStatus.OK).body(memberEntity);
    }

    @GetMapping("/check-email")
    @Operation(summary = "check email", description = "Check whether this email already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        memberService.checkEmail(email);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "check nickname", description = "Check whether this nickname already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        memberService.checkNickname(nickname);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/signout")
    @Operation(summary = "signout", description = "Member signout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Signout success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized member"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Void> signout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("User is not authenticated");
        }

        String email = authentication.getName();
        memberService.signOut(email);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/members/{memberId}")
    @Operation(summary = "delete member", description = "Delete member")
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
