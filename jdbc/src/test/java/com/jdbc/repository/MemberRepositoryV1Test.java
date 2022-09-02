package com.jdbc.repository;

import com.jdbc.domain.Member;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
class MemberRepositoryV1Test {

    MemberRepositoryV1 repositoryV1;
    String memeberId = "memberV2";

    @BeforeEach
    void beforeEach() throws SQLException {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        /**
         * HikariDataSource pool 설정 및 connection 설정
         * */
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);
        dataSource.setPoolName("poolTest");

        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

    @Test
    void order1_save() throws SQLException {
        Member memberV0 = new Member(memeberId, 10000);
        repositoryV1.save(memberV0);
        assertThat(memberV0).isNotNull();
        log.info("[save]");
    }

    @Test
    void order2_findById() throws SQLException {
        Member member = repositoryV1.findById(memeberId);

        log.info("[Test] member(memberId = {}, money = {})", member.getMemberId(), member.getMoney());
        assertThat(member).isNotNull();
        log.info("[find]");

//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    void order3_update() throws SQLException {
        repositoryV1.update(memeberId, 20000);
        Member updatedMember = repositoryV1.findById(memeberId);
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
        log.info("[update]");
    }

    @Test
    void order4_remove() throws SQLException {
        repositoryV1.delete(memeberId);
        log.info("[delete success]");
    }
}