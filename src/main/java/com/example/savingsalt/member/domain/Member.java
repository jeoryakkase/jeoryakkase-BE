package com.example.savingsalt.member.domain;

import com.example.savingsalt.global.BaseEntity;
import com.example.savingsalt.member.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "member")
@EntityListeners(AuditingEntityListener.class)
public class Member extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = true) // 소셜 로그인일 경우 비밀번호가 없음
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "gender", nullable = false)
    private int gender;

    @Column(name = "income", nullable = false)
    private int income;

    @Column(name = "saving_goal", nullable = false)
    private int savingGoal;

    @Column(name = "profile_image", nullable = true)
    private String profileImage;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

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

    public MemberDto toDto() {
        return MemberDto.builder()
            .id(this.id)
            .email(this.email)
            .password(this.password)
            .nickname(this.nickname)
            .age(this.age)
            .gender(this.gender)
            .income(this.income)
            .saving_goal(this.savingGoal)
            .build();
    }
}
