package com.jdbc.repository;

import com.jdbc.domain.Member;
import com.jdbc.repository.exception.MyDbException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static com.jdbc.connect.DBHelper.close;


/**
 * SQLExceptionTranslator 추가
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository{
    private final DataSource dataSource;
    private final SQLExceptionTranslator translator;

    public MemberRepositoryV4_2(DataSource dataSource) {
        this.dataSource = dataSource;
        this.translator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    public Member save(Member member){
        String sql = "insert into member(member_id, money) values (?,?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            throw translator.translate("save", sql, e);
//            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null, dataSource);
        }
    }

    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }
            throw new NoSuchElementException("member not found memberId = " + memberId);
        } catch (SQLException e) {
            throw translator.translate("findById", sql, e);
//            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null, dataSource);
        }
    }

    /**
     * transaction Connection
     * @param con
     * @param memberId
     * @return
     * @throws SQLException
     */
    public Member findById(Connection con,String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            }
            throw new NoSuchElementException("member not found memberId = " + memberId);
        } catch (SQLException e) {
            throw translator.translate("findById", sql, e);
//            log.info("db error", e);
//            throw e;
        } finally {
            close(con, pstmt, null, dataSource);
        }
    }

    public void update(String memberId, int money) {
        String sql = "update member set money = ? where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);;
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);

        } catch (SQLException e) {
            throw translator.translate("update", sql, e);
//            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null, dataSource);
        }
    }

    public void delete(String memberId) {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw translator.translate("delete", sql, e);
//            throw new MyDbException(e);
        } finally {
            close(con, pstmt, null, dataSource);
        }
    }

    private Connection getConnection() throws SQLException{
//        warning!! 트랜잭션 동기화 사용시 DataSourceUtils사용
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("getConnection = {} , class ={}", con, getConnection());
        return con;
    }
}
