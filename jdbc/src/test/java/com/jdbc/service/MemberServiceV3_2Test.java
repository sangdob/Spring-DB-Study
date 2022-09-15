
package com.jdbc.service;

import com.jdbc.domain.Member;
import com.jdbc.repository.MemberRepositoryV3;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - connection 동기화 테스트
 */
@Slf4j
@SpringBootTest
class MemberServiceV3_2Test {

    public static final String MEMBER_A = "accountTestA";
    public static final String MEMBER_B = "accountTestB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV3 repositoryV3;
    private MemberServiceV3_2 serviceV3;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        repositoryV3 = new MemberRepositoryV3(dataSource);
        serviceV3 = new MemberServiceV3_2(transactionManager, repositoryV3);
    }

    @AfterEach
    void after() throws SQLException {
        repositoryV3.delete(MEMBER_A);
        repositoryV3.delete(MEMBER_B);
        repositoryV3.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체 상황")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        repositoryV3.save(memberA);
        repositoryV3.save(memberB);

        //when
        serviceV3.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 6000);

        //then
        Member findMemberA = repositoryV3.findById(memberA.getMemberId());
        Member findMemberB = repositoryV3.findById(memberB.getMemberId());
        log.info("find member A  = {} ", findMemberA.toString());
        log.info("find member A  = {} ", findMemberB.toString());

        assertThat(findMemberA.getMoney()).isEqualTo(4000);
        assertThat(findMemberB.getMoney()).isEqualTo(16000);
    }

    @Test
    @DisplayName("이체 예외 상황")
    void accountTransferEx() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        repositoryV3.save(memberA);
        repositoryV3.save(memberEx);

        //when
        assertThatThrownBy(() -> {
            serviceV3.accountTransfer(memberA.getMemberId(),
                memberEx.getMemberId(),
                20000);}).isInstanceOf(IllegalStateException.class);
//        serviceV1.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 6000);

        //then
   /*     Member findMemberA = repositoryV1.findById(memberA.getMemberId());
        Member findMemberB = repositoryV1.findById(memberEx.getMemberId());
        log.info("find member A  = {} ", findMemberA.toString());
        log.info("find member A  = {} ", findMemberB.toString());

        assertThat(findMemberA.getMoney()).isEqualTo(4000);
        assertThat(findMemberB.getMoney()).isEqualTo(16000);*/
    }

}