package com.example.savingsalt.member.controller;

import com.example.savingsalt.config.jwt.JwtTokenProvider;
import com.example.savingsalt.global.UnauthorizedException;
import com.example.savingsalt.member.domain.LoginRequestDto;
import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.domain.MemberUpdateRequestDto;
import com.example.savingsalt.member.domain.MemberUpdateResponseDto;
import com.example.savingsalt.member.domain.MyPageResponseDto;
import com.example.savingsalt.member.domain.OAuth2SignupRequestDto;
import com.example.savingsalt.member.domain.SignupRequestDto;
import com.example.savingsalt.member.domain.TokenResponseDto;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.exception.MemberException.InvalidTokenException;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMapper;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberMyPageMapper;
import com.example.savingsalt.member.mapper.MemberMainMapper.MemberUpdateMapper;
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
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
    private final MemberMapper memberMapper;
    private final MemberUpdateMapper memberUpdateMapper;
    private final MemberMyPageMapper myPageMapper;

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

    @PostMapping("/signup/additional-info")
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

    @PostMapping("/login")
    @Operation(summary = "login", description = "Member login")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Login success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
        TokenResponseDto tokenResponseDto = memberService.login(dto);
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponseDto.getAccessToken())
            .body(tokenResponseDto.getRefreshToken());
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

    @GetMapping("/members/update-info")
    @Operation(summary = "get member update info page", description = "Get member info page for update")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<MemberUpdateResponseDto> getMemberUpdatePage(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        MemberUpdateResponseDto responseDto = memberUpdateMapper.toDto(memberEntity);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/members/mypage")
    @Operation(summary = "get mypage", description = "Get member mypage")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<MyPageResponseDto> getMyPage(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new MemberException.InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        MemberEntity memberEntity = memberMapper.toEntity(memberService.findMemberByEmail(email));
        if (memberEntity == null) {
            throw new MemberException.MemberNotFoundException("email", email);
        }

        MyPageResponseDto responseDto = myPageMapper.toDto(memberEntity);
        return ResponseEntity.ok(responseDto);
    }


    @PatchMapping("/members/update")
    @Operation(summary = "member update", description = "Update member info")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Update success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "404", description = "Member not found"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> updateMember(HttpServletRequest request,
        @RequestBody MemberUpdateRequestDto dto) {
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }

        String email = tokenProvider.getEmailFromToken(token);
        Long memberId = memberService.findMemberByEmail(email).getId();

        MemberEntity memberEntity = memberService.updateMember(memberId, dto.getEmail(),
            dto.getPassword(),
            dto.getNickname(), dto.getAge(),
            dto.getGender(), dto.getIncome(), dto.getSavePurpose(), dto.getProfileImage(),
            dto.getInterests(), dto.getAbout());

        return ResponseEntity.status(HttpStatus.OK).body(memberEntity);
    }

    @GetMapping("/check-email")
    @Operation(summary = "check email", description = "Check whether this email already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "This email already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        try {
            boolean isAvailable = memberService.checkEmail(email);
            return ResponseEntity.ok().body(isAvailable);
        } catch (MemberException.EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }
    }

    @GetMapping("/check-nickname")
    @Operation(summary = "check nickname", description = "Check whether this nickname already exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Check success"),
        @ApiResponse(responseCode = "400", description = "Bad request"),
        @ApiResponse(responseCode = "409", description = "This nickname already exists"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        try {
            boolean isAvailable = memberService.checkNickname(nickname);
            return ResponseEntity.ok().body(isAvailable);
        } catch (MemberException.EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }
    }

    @DeleteMapping("members/signout")
    @Operation(summary = "signout", description = "Member signout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Signout success"),
        @ApiResponse(responseCode = "401", description = "Unauthorized member"),
        @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Void> signout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new UnauthorizedException("User is not authenticated");
        }

        String email = tokenProvider.getEmailFromToken(token);
        Long memberId = memberService.findMemberByEmail(email).getId();

        memberService.signOut(memberId);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/members/{memberId}")
    @Operation(summary = "delete member by admin", description = "Delete member")
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
