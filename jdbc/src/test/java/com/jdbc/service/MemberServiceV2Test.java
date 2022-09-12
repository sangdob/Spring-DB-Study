
package com.jdbc.service;

import com.jdbc.domain.Member;
import com.jdbc.repository.MemberRepositoryV2;
import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - 커넥션 파라미터 전달 형식 테스트
 */
@Slf4j
@SpringBootTest
class MemberServiceV2Test {

    public static final String MEMBER_A = "accountTestA";
    public static final String MEMBER_B = "accountTestB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV2 repositoryV2;
    private MemberServiceV2 serviceV2;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        repositoryV2 = new MemberRepositoryV2(dataSource);
        serviceV2 = new MemberServiceV2(dataSource, repositoryV2);
    }

    @AfterEach
    void after() throws SQLException {
        repositoryV2.delete(MEMBER_A);
        repositoryV2.delete(MEMBER_B);
        repositoryV2.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체 상황")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        repositoryV2.save(memberA);
        repositoryV2.save(memberB);

        //when
        serviceV2.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 6000);

        //then
        Member findMemberA = repositoryV2.findById(memberA.getMemberId());
        Member findMemberB = repositoryV2.findById(memberB.getMemberId());
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
        repositoryV2.save(memberA);
        repositoryV2.save(memberEx);

        //when
        assertThatThrownBy(() -> {
            serviceV2.accountTransfer(memberA.getMemberId(),
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