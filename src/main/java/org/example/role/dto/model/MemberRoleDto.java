package org.example.role.dto.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberRoleDto {

    @NotNull
    private String roleCode;

    @NotNull
    private String roleName;

    private String description;

    private LocalDateTime regDt;

    private LocalDateTime updDt;

    private boolean newMemberRole = true;

}
