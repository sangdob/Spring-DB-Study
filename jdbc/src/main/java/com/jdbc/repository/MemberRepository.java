package com.jdbc.repository;

import com.jdbc.domain.Member;

public interface MemberRepository {
    Member save(Member member);
    Member findById(String id);
    void update(String id, int money);
    void delete(String id);
}
