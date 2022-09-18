package com.jdbc.service;

import com.jdbc.domain.Member;
import com.jdbc.repository.MemberRepositoryV3;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;

/**
 * 트랜잭션 - 트랜잭션 템프릿
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_2 {

    private final TransactionTemplate template;
    private final MemberRepositoryV3 memberRepositoryV3;

    public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepositoryV3) {
        this.template = new TransactionTemplate(transactionManager);
        this.memberRepositoryV3 = memberRepositoryV3;
    }

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        template.executeWithoutResult(transactionStatus -> {
            try {
                bizLogic(fromId, toId, money);
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        });
    }

    private void bizLogic( String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepositoryV3.findById(fromId);
        Member toMember = memberRepositoryV3.findById(toId);
        memberRepositoryV3.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepositoryV3.update(toId, toMember.getMoney() + money);
    }


    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체중 예외 발생");
        }
    }
}
