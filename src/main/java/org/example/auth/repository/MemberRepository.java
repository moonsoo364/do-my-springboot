package org.example.auth.repository;

import org.example.auth.model.Member;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MemberRepository extends ReactiveCrudRepository<Member,String> {
    Mono<Member> findByUserId(String userId);

    @Modifying
    @Query("DELETE FROM member WHERE user_id = :userId")
    Mono<Integer> deleteByUserIdReturnCount(String userId);
}
