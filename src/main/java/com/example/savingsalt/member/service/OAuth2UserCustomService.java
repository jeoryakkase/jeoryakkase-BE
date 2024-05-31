package com.example.savingsalt.member.service;

import com.example.savingsalt.member.domain.MemberEntity;
import com.example.savingsalt.member.enums.Role;
import com.example.savingsalt.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
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
        boolean isNewUser = saveOrUpdate(user);
        if (isNewUser) {
            // 세션에 새 사용자 정보 저장 (성공 핸들러에서 확인하기 위해)
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
                .getSession();
            session.setAttribute("newUser", true);
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
        return false; // 기존 회원 -> 소셜 로그인
    }
}
