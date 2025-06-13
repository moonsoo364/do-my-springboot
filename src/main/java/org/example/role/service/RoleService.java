package org.example.role.service;

import org.example.role.model.MemberRole;
import org.example.role.model.MemberRolePermission;
import reactor.core.publisher.Mono;

public interface RoleService {

    Mono<MemberRole> saveMemberRole(MemberRole memberRole);

    Mono<MemberRolePermission> saveMemberRolePermission(MemberRolePermission memberRolePermission);

}
