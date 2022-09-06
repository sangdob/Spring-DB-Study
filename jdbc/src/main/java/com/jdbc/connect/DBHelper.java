package com.jdbc.connect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DBHelper {
    public static void close(Statement stmt, ResultSet rs) {
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeResultSet(rs);
    }

    public static void close(Connection con, Statement stmt, ResultSet rs) {

        /**
         * JdbcUtils 활용 close
         * */
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);

      /*  if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("[ resultSet error] ", e);
                e.printStackTrace();
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("[ statement error] ", e);
                e.printStackTrace();
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("[connection error] ", e);
                e.printStackTrace();
            }
        }*/
    }

    public static Connection getConnection() {
        return DBConnectionUtil.getConnection();
    }

    public static Connection getConnection(DataSource dataSource) throws SQLException {
        Connection connection = dataSource.getConnection();
        log.info("get Connection = {} , class = {} ", connection,connection.getClass());
        return connection;
    }
}
