package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.enums.Role;
import com.example.savingsalt.member.exception.MemberException;
import com.example.savingsalt.member.repository.MemberRepository;
import com.example.savingsalt.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Service
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        // 일반 회원가입으로 가입했던 이메일인지 확인
        String email = (String) user.getAttributes().get("email");
        if (memberRepository.existsByEmail(email)) {
            throw new MemberException.EmailAlreadyExistsException("This email is registered with normal login.");
        }

        // 새로운 회원인지 확인
        boolean isNewUser = saveOrUpdate(user);

        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        if (isNewUser) {
            // 쿠키에 새 사용자 정보 저장 (성공 핸들러에서 확인하기 위해)
            CookieUtil.addCookie(response, "newUser", "true", 18000);
        }
        return user;
    }

    private boolean saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");

        MemberEntity memberEntity = memberRepository.findByEmail(email).orElse(null);
        if (memberEntity == null) {
            memberEntity = MemberEntity.builder()
                .email(email)
                .role(Role.MEMBER)
                .build();
            memberRepository.save(memberEntity);
            return true; // 새로운 소셜 회원 -> 회원가입
        }
        return false; // 기존 소셜 회원 -> 소셜 로그인
    }
}
