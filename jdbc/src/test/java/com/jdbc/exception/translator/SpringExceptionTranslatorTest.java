package com.jdbc.exception.translator;

import com.jdbc.connect.ConnectionConst;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static com.jdbc.connect.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
public class SpringExceptionTranslatorTest {

    DataSource dataSource;

    @BeforeEach
    void init() {
        dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    /**
     * SqlException
     */
    @Test
    void sqlExceptionErrorCode() {
        String sql = "select bad grammer";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstst = connection.prepareStatement(sql);
            pstst.executeQuery();
        } catch (SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(0);
            int errorCode = e.getErrorCode();
            log.info("errorCode = {}", errorCode);
        }
    }

    /**
     * 스프링에서 제공하는 SQL 예외 변환기
     */
    @Test
    void exceptionTranslator() {
        String sql = "select bad grammer";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement pstst = connection.prepareStatement(sql);
            pstst.executeQuery();
        } catch (SQLException e) {
            assertThat(e.getErrorCode()).isEqualTo(0);
            SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
            DataAccessException resultEx = translator.translate("select", sql, e);
            log.info("resultEx", resultEx);
            assertThat(resultEx.getClass()).isEqualTo(BadSqlGrammarException.class);
        }
    }
}
