package com.jdbc.repository;

import com.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();
    String memeberId = "memberV1";

    @BeforeEach
    void beforeEach() {
    }

    @AfterEach
    void afterEach() throws SQLException{
        repositoryV0.remove(memeberId);
        log.info("[delete success]");
    }

    @Test
    void save() throws SQLException {
        Member memberV0 = new Member(memeberId, 10000);
        repositoryV0.save(memberV0);

        Member member = repositoryV0.findById(memeberId);

        log.info("[Test] member(memberId = {}, money = {})", member.getMemberId(), member.getMoney());
        Assertions.assertThat(member).isNotNull();
    }

}