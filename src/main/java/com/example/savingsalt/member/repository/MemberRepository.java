package com.example.savingsalt.member.repository;

import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.enums.Gender;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByNickname(String nickname);

    // 평균을 내기 위한 메서드
    List<MemberEntity> findAllByAgeBetween(int startAge, int endAge);

    List<MemberEntity> findAllByGender(Gender gender);

    List<MemberEntity> findAllByIncomeBetween(int startIncome, int endIncome);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
