package org.example.auth.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.dto.CheckUserDto;
import org.example.auth.dto.MemberDto;
import org.example.auth.model.Member;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberDao {
    final R2dbcEntityTemplate entityTemplate;

    public Mono<Member> findUserByUserId(String userId){
        //SELECT member.* FROM member WHERE member.user_id = ? LIMIT 2 : 실제 쿼리에서 프로젝션은 없음
        return entityTemplate.select(Member.class)
                .from("member")
                .matching(Query.query(Criteria.where("user_id").is(userId)))
                .one();

    }

    public Mono<MemberDto> findUserProjectionByUserId(String userId){
        String sql = "SELECT " +
                "user_id, " +
                "member_name," +
                "role_code," +
                "locale_code " +
                "FROM member " +
                "WHERE user_id = :userId";
        return entityTemplate.getDatabaseClient().sql(sql)
                .bind("userId",userId)
                .map(
                        (row, metadata)
                                -> {
                            return MemberDto.builder()
                                    .userId(row.get("user_id",String.class))
                                    .memberName(row.get("member_name",String.class))
                                    .roleCode(row.get("role_code",String.class))
                                    .localeCode(row.get("locale_code", String.class))
                                    .build();
                        }
                )
                .one();
    }
}
