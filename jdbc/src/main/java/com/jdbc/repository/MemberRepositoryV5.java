package com.jdbc.repository;

import com.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

@Slf4j
public class MemberRepositoryV5 implements MemberRepository{
    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        String sql = "insert into member(member_id, money) values (?, ?)";
        int update = template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public Member findById(String id) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), id);
    }

    @Override
    public void update(String id, int money) {
        String sql = "update member set money = ? where member_id = ?";
        template.update(sql, money, id);
    }

    @Override
    public void delete(String id) {
        String sql = "delete from member where member_id = ?";
        template.update(sql, id);
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }
}
