package com.jdbc.connection;

import com.jdbc.connect.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;

@Slf4j
@SpringBootTest
public class ConnectionTest {

    @Test
    void driverManager() throws SQLException {
        Connection connection1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        Connection connection2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        log.info("connection1={}, class={}",connection1, connection1.getClass());
        log.info("connection2={}, class={}",connection2, connection2.getClass());
    }
}
