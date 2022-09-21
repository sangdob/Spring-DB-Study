package com.jdbc.exception.translator;

import com.jdbc.DBConfig;
import com.jdbc.connect.ConnectionConst;
import com.jdbc.connect.DBConnectionUtil;
import com.jdbc.connect.DBHelper;
import com.jdbc.domain.Member;
import com.jdbc.repository.MemberRepository;
import com.jdbc.repository.exception.MyDbException;
import com.jdbc.repository.exception.MyDuplicationKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static com.jdbc.connect.ConnectionConst.*;
import static com.jdbc.connect.DBHelper.*;

@Slf4j
@SpringBootTest
public class ExTranslatorV1Test {
    Repository repository;
    Service service;

    @BeforeEach
    void init() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave() {
        service.create("test5");
        service.create("test5");
    }

    @Slf4j
    @RequiredArgsConstructor
    static class Service {
        private final Repository repository;

        public void create(String memberId) {

            try {
                repository.save(new Member(memberId, 0));
                log.info("save ID = {} ", memberId);
            } catch (MyDuplicationKeyException e) {
                log.info("key duplicate error");
                String retryId = generateNewId(memberId);
                log.info("retry ID = {}", retryId);
                repository.save(new Member(retryId, 0));
            } catch (MyDbException e) {
                log.info("db접근 계층 예외", e);
                throw e;
            }

        }

        private String generateNewId(String memberId) {
            return memberId + new Random().nextInt();
        }
    }

    @RequiredArgsConstructor
    static class Repository implements MemberRepository {
        private final DataSource dataSource;

        @Override
        public Member save(Member member) {
            String sql = "insert into member(member_id, money) values(?,?)";

            Connection connection = null;
            PreparedStatement pstmt = null;

            try {
                connection = dataSource.getConnection();
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            } catch (SQLException e) {
                log.info("error Code = {}", e.getErrorCode());
                if (e.getErrorCode() == 0) {
                    throw new MyDuplicationKeyException(e);
                }
                throw new MyDbException(e);
            }finally {
                close(connection, pstmt, null);
            }

        }

        @Override
        public Member findById(String id) {
            return null;
        }

        @Override
        public void update(String id, int money) {

        }

        @Override
        public void delete(String id) {

        }
    }

}
