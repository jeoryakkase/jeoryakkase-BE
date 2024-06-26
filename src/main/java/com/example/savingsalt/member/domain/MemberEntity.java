package com.example.savingsalt.member.domain;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.enums.Gender;
import com.example.savingsalt.member.enums.Role;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = true) // 소셜 로그인일 경우 비밀번호가 없음
    private String password;

    @Column(name = "nickname", nullable = true) // 소셜 회원가입을 할 경우 나중에 입력
    private String nickname;

    @Column(name = "age", nullable = true)
    private int age;

    @Column(name = "gender", nullable = true)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "income", nullable = true)
    private int income;

    @Column(name = "save_purpose", nullable = true)
    private String savePurpose;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Column(name = "interests", nullable = true)
    @ElementCollection
    private List<Long> interests;

    @Column(name = "about", nullable = true)
    private String about;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "memberEntity", cascade = CascadeType.ALL)
    private List<MemberChallengeEntity> memberChallengeEntities;

    @Column(name = "representative_badge_id", nullable = true)
    private Long representativeBadgeId;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void authorizeUser() {
        this.role = Role.MEMBER;
    }
}
