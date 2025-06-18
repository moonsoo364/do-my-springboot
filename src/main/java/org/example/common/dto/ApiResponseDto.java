package org.example.common.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.common.consts.ResultCode;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ApiResponseDto {
    @JsonIgnore
    private HttpStatus httpStatus = HttpStatus.OK;
    private int resultCode;
    private String resultMsg;

    public ApiResponseDto(){
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        resultCode = ResultCode.FAIL;
        resultMsg = "Unexpected error";
    }

    //HttpStatus == 200, 성공 처리 생성자
    public ApiResponseDto(int resultCode, String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
