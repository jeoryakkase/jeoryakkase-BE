package com.example.savingsalt.member.repository;

import com.example.savingsalt.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    // 평균을 내기 위한 메서드
    List<Member> findByAgeBetween(int startAge, int endAge);

    List<Member> findByGender(int gender);

    List<Member> findByIncomeBetween(int startIncome, int endIncome);

    boolean existsMemberByEmail(String email);

    boolean existsMemberByNickname(String nickname);
}
