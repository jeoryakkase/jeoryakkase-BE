package com.example.savingsalt.member.repository;

import com.example.savingsalt.member.domain.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByNickname(String nickname);

    // 평균을 내기 위한 메서드
    List<MemberEntity> findByAgeBetween(int startAge, int endAge);

    List<MemberEntity> findByGender(int gender);

    List<MemberEntity> findByIncomeBetween(int startIncome, int endIncome);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
