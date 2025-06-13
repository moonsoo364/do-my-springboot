package org.example.role.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.role.dto.model.MemberRoleDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@Slf4j
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table("member_role")
public class MemberRole implements Persistable<String> {

    @Id
    @Column("role_code")
    private String roleCode;

    @NotNull
    @Column("role_name")
    private String roleName;

    @Column("description")
    private String description;

    @Column("reg_dt")
    private LocalDateTime regDt;

    @Column("upd_dt")
    private LocalDateTime updDt;

    @Transient
    private boolean newMemberRole;

    public MemberRole(MemberRoleDto dto){
        roleCode = dto.getRoleCode();
        roleName = dto.getRoleName();
        description = dto.getDescription();
        newMemberRole = dto.isNewMemberRole();
    }

    @Override
    public String getId() {
        return roleCode;
    }

    @Override
    public boolean isNew() {
        return newMemberRole || roleCode == null;
    }
}
