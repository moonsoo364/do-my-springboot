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
        resultCode = ResultCode.FAIL;
        resultMsg = "Unexpected error";
    }

    public ApiResponseDto(int resultCode, String resultMsg){
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }
}
