package com.jdbc.repository;

import com.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @Test
    void save() throws SQLException {
        Member memberV0 = new Member("memberV0", 10000);
        repositoryV0.save(memberV0);
    }

    @Test
    void findById() throws SQLException {
        Member member = repositoryV0.findById("memberV0");

        log.info("[Test] member(memberId = {}, money = {})", member.getMemberId(), member.getMoney());
        Assertions.assertThat(member).isNotNull();
    }
}