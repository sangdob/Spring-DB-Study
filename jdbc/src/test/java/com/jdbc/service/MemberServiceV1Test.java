package com.jdbc.service;

import com.jdbc.domain.Member;
import com.jdbc.repository.MemberRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

/**
 * 기본 동작, 트랜잭션 없는 문제 발생 테스트
 */
@Slf4j
@SpringBootTest
class MemberServiceV1Test {

    public static final String MEMBER_A = "accountTestA";
    public static final String MEMBER_B = "accountTestB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV1 repositoryV1;
    private MemberServiceV1 serviceV1;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        repositoryV1 = new MemberRepositoryV1(dataSource);
        serviceV1 = new MemberServiceV1(repositoryV1);
    }

    @AfterEach
    void after() throws SQLException {
        repositoryV1.delete(MEMBER_A);
        repositoryV1.delete(MEMBER_B);
        repositoryV1.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상 이체 상황")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        repositoryV1.save(memberA);
        repositoryV1.save(memberB);

        //when
        serviceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 6000);

        //then
        Member findMemberA = repositoryV1.findById(memberA.getMemberId());
        Member findMemberB = repositoryV1.findById(memberB.getMemberId());
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
        repositoryV1.save(memberA);
        repositoryV1.save(memberEx);

        //when
        assertThatThrownBy(() -> {
            serviceV1.accountTransfer(memberA.getMemberId(),
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