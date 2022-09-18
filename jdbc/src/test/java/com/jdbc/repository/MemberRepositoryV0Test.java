package com.jdbc.repository;

import com.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();
    String memeberId = "memberV2";

    @Test
    void order1_save() throws SQLException {
        Member memberV0 = new Member(memeberId, 10000);
        repositoryV0.save(memberV0);
        assertThat(memberV0).isNotNull();
        log.info("[save]");
    }

    @Test
    void order2_findById() throws SQLException {
        Member member = repositoryV0.findById(memeberId);

        log.info("[Test] member(memberId = {}, money = {})", member.getMemberId(), member.getMoney());
        assertThat(member).isNotNull();
        log.info("[find]");
    }

    @Test
    void order3_update() throws SQLException {
        repositoryV0.update(memeberId, 20000);
        Member updatedMember = repositoryV0.findById(memeberId);
        assertThat(updatedMember.getMoney()).isEqualTo(20000);
        log.info("[update]");
    }

    @Test
    void order4_remove() throws SQLException {
        repositoryV0.remove(memeberId);
        log.info("[delete success]");
    }
}