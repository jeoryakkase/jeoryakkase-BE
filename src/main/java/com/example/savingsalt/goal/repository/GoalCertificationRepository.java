package com.example.savingsalt.goal.repository;

import com.example.savingsalt.goal.domain.entity.GoalCertificationEntity;
import com.example.savingsalt.goal.domain.entity.GoalEntity;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalCertificationRepository extends JpaRepository<GoalCertificationEntity, Long> {

    // 목표 엔티티를 기준으로 인증 목록을 페이지네이션 및 인증 날짜 기준 내림차순으로 가져옴
    Page<GoalCertificationEntity> findAllByGoalEntityOrderByCertificationDateDesc(
        GoalEntity goalEntity, Pageable pageable);

    @Query("SELECT SUM(gc.certificationMoney) FROM GoalCertificationEntity gc WHERE gc.memberEntity = :member AND gc.certificationDate = :date")
    Long sumDailyCertificationMoneyByMember(MemberEntity member, LocalDate date);

    @Query("SELECT SUM(gc.certificationMoney) FROM GoalCertificationEntity gc WHERE gc.memberEntity = :member AND MONTH(gc.certificationDate) = :month AND YEAR(gc.certificationDate) = :year")
    Long sumMonthlyCertificationMoneyByMember(MemberEntity member, int month, int year);

    @Query("SELECT SUM(gc.certificationMoney) FROM GoalCertificationEntity gc WHERE gc.memberEntity = :member")
    Long sumAllCertificationMoneyByMember(MemberEntity member);
}
