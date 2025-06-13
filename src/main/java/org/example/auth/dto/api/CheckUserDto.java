package org.example.auth.dto.api;

import org.example.auth.dto.model.MemberDto;

public record CheckUserDto(
        String userId,
        String memberName,
        Boolean isExistsUser
){

    public CheckUserDto(MemberDto member){
        this(member.getUserId(), member.getMemberName(), true);
    }
//
}
