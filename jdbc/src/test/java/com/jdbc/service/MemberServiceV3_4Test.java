
package com.jdbc.service;

import com.jdbc.domain.Member;
import com.jdbc.repository.MemberRepository;
import com.jdbc.repository.MemberRepositoryV4_1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 트랜잭션 - connection 동기화 테스트
 */
@Slf4j
@SpringBootTest
class MemberServiceV3_4Test {

    public static final String MEMBER_A = "accountTestA";
    public static final String MEMBER_B = "accountTestB";
    public static final String MEMBER_EX = "ex";

    @Autowired
    private MemberRepository repository;

    @Autowired
    private MemberServiceV4 service;

    @TestConfiguration
    static class TestConfig {
        private final DataSource dataSource;

        public TestConfig(DataSource dataSource) {
            this.dataSource = dataSource;
        }

        @Bean
        DataSource getDataSource() {
            return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        }

        @Bean
        PlatformTransactionManager transactionManager() {
            return new DataSourceTransactionManager(dataSource);
        }

        @Bean
        MemberServiceV4 memberServiceV3() {
            return new MemberServiceV4(memberRepository());
        }

        @Bean
        MemberRepository memberRepository(){
            return new MemberRepositoryV4_1(getDataSource());
        }
    }

    @AfterEach
    void after() {
        repository.delete(MEMBER_A);
        repository.delete(MEMBER_B);
        repository.delete(MEMBER_EX);
    }

    @Test
    void AopCheck() {
        log.info("service = {}", service.getClass());
        log.info("repository = {}", repository.getClass());
        assertThat(AopUtils.isAopProxy(service)).isTrue();
        assertThat(AopUtils.isAopProxy(repository)).isTrue();
    }

    @Test
    @DisplayName("정상 이체 상황")
    void accountTransfer() {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberB = new Member(MEMBER_B, 10000);
        repository.save(memberA);
        repository.save(memberB);

        //when
        service.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 6000);

        //then
        Member findMemberA = repository.findById(memberA.getMemberId());
        Member findMemberB = repository.findById(memberB.getMemberId());
        log.info("find member A  = {} ", findMemberA.toString());
        log.info("find member B  = {} ", findMemberB.toString());

        assertThat(findMemberA.getMoney()).isEqualTo(4000);
        assertThat(findMemberB.getMoney()).isEqualTo(16000);
    }

    @Test
    @DisplayName("이체 예외 상황")
    void accountTransferEx() {
        //given
        Member memberA = new Member(MEMBER_A, 10000);
        Member memberEx = new Member(MEMBER_EX, 10000);
        repository.save(memberA);
        repository.save(memberEx);

        //when
        assertThatThrownBy(() -> {
            service.accountTransfer(memberA.getMemberId(),
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