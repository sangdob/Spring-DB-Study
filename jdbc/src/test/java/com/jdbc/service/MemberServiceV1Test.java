package com.jdbc.service;

import com.jdbc.connect.ConnectionConst;
import com.jdbc.repository.MemberRepositoryV1;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static com.jdbc.connect.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 기본 동작, 트랜잭션 없는 문제 발생 테스트
 */
@Slf4j
@SpringBootTest
class MemberServiceV1Test {

    public static final String MEMBER_A = "transactionTestA";
    public static final String MEMBER_B = "transactionTestB";
    public static final String MEMBER_EX = "ex";

    private MemberRepositoryV1 repositoryV1;
    private MemberServiceV1 serviceV1;

    @BeforeEach
    void before() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

        serviceV1 = new MemberServiceV1(repositoryV1);
        repositoryV1 = new MemberRepositoryV1(dataSource);
    }

}