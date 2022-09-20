package com.jdbc.repository;

import com.jdbc.domain.Member;

public interface MemberRepositoryEx {
    Member save(Member member) throws Exception;
    Member findById(String id) throws Exception;
    void update(String id, int money) throws Exception;
    void delete(String id) throws Exception;
}
