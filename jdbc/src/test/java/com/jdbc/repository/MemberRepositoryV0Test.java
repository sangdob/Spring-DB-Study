package com.jdbc.repository;

import com.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();
    String memeberId = "memberV2";

    @Test
    @Order(1)
    void save() throws SQLException {
        Member memberV0 = new Member(memeberId, 10000);
        repositoryV0.save(memberV0);

    }

    @Test
    @Order(2)
    void findById() throws SQLException {
        Member member = repositoryV0.findById(memeberId);

        log.info("[Test] member(memberId = {}, money = {})", member.getMemberId(), member.getMoney());
        assertThat(member).isNotNull();
    }

    @Test
    @Order(3)
    void update() throws SQLException {
        repositoryV0.update(memeberId, 20000);
        Member updatedMember = repositoryV0.findById(memeberId);
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
    }

    @Test
    @Order(4)
    void remove() throws SQLException {
        repositoryV0.remove(memeberId);
        log.info("[delete success]");
    }
}