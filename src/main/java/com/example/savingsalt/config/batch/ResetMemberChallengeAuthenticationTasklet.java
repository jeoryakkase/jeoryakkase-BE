package com.example.savingsalt.config.batch;

import com.example.savingsalt.challenge.domain.entity.MemberChallengeEntity;
import com.example.savingsalt.challenge.repository.MemberChallengeRepository;
import com.example.savingsalt.member.domain.entity.MemberEntity;
import com.example.savingsalt.member.repository.MemberRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class ResetMemberChallengeAuthenticationTasklet implements Tasklet {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberChallengeRepository memberChallengeRepository;


    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {
        log.info("챌린지 인증 초기화 실행");

        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            List<MemberChallengeEntity> memberChallengeEntities = memberEntity.getMemberChallengeEntities();

            for (MemberChallengeEntity memberChallengeEntity : memberChallengeEntities) {
                memberChallengeEntity = memberChallengeEntity.toBuilder()
                    .isTodayCertification(false)
                    .build();

                memberChallengeRepository.save(memberChallengeEntity);
            }
        }
        return RepeatStatus.FINISHED;
    }

}
