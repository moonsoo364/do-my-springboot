package org.example.auth.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.auth.model.Member;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto  {

    @NotBlank
    private String userId;

    @NotBlank
    private String memberName;

    @NotBlank
    private String password;

    private String roleCode;

    private String localeCode;

    @JsonIgnore
    private boolean newMember;


    @JsonIgnore
    public MemberDto (Member member){
        this.memberName = member.getMemberName();
        this.userId = member.getUserId();
        this.localeCode = member.getLocaleCode();
        this.roleCode = member.getRoleCode();
    }

}
