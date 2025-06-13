package org.example.role.dto.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRolePermissionDto {

    private Long id;

    private String roleCode;

    private String urlPattern;

    private String crudType;

    private LocalDateTime regDt;

    private LocalDateTime updDt;

}
