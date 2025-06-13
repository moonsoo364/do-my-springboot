package org.example.role.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("member_role_permission")
public class MemberRolePermission {

    @Id
    @Column("id")
    private Long id;

    @Column("role_code")
    private String roleCode;

    @Column("url_pattern")
    private String urlPattern;

    @Column("crud_type")
    private String crudType;

    @Column("reg_dt")
    private LocalDateTime regDt;

    @Column("upd_dt")
    private LocalDateTime updDt;

}
