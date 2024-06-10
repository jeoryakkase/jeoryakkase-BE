package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.enums.Role;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email = null;
        if(registrationId.equals("google")) {
            email = (String) user.getAttributes().get("email");
        } else if (registrationId.equals("kakao")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) user.getAttributes().get("kakao_account");
            email = kakaoAccount.get("email").toString();
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider.");
        }

        // 일반 회원가입으로 가입했던 이메일인지 확인
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException.EmailAlreadyExistsException("This email is registered with normal login.");
        }

        // 새로운 회원인지 확인
//        boolean isNewUser = saveOrUpdate(user, email);
//
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//        if (isNewUser) {
//            // 쿠키에 새 사용자 정보 저장 (성공 핸들러에서 확인하기 위해)
//            CookieUtil.addCookie(response, "newUser", "true", 18000);
//        }

        saveOrUpdate(user, email);
        return user;
    }

    private void saveOrUpdate(OAuth2User oAuth2User, String email) {
        MemberEntity memberEntity = memberRepository.findByEmail(email).orElse(null);
        if (memberEntity == null) {
            memberEntity = MemberEntity.builder()
                .email(email)
                .role(Role.MEMBER)
                .build();
            memberRepository.save(memberEntity);
//            return true; // 새로운 소셜 회원 -> 회원가입
        }
//        return false; // 기존 소셜 회원 -> 소셜 로그인
    }
}
