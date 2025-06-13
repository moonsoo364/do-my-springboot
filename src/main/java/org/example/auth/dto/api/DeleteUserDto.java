package org.example.auth.dto.api;

public record DeleteUserDto(
        Integer delCnt,
        String resultMsg
) {
    public static DeleteUserDto fromDeleteCnt(Integer delCnt){
        if(delCnt == null || delCnt == 0){
            return new DeleteUserDto(0, "Fail to delete user");
        }else{
            return new DeleteUserDto(delCnt, "Success to delete user");
        }
    }
}

