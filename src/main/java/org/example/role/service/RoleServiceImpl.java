package org.example.role.service;

import lombok.AllArgsConstructor;
import org.example.role.model.MemberRole;
import org.example.role.model.MemberRolePermission;
import org.example.role.repository.MemberRolePermissionRepository;
import org.example.role.repository.MemberRoleRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final MemberRoleRepository memberRoleRepo;

    private final MemberRolePermissionRepository memberRolePermissionRepo;


    @Override
    public Mono<MemberRole> saveMemberRole(MemberRole memberRole) {
        return memberRoleRepo.save(memberRole);
    }

    @Override
    public Mono<MemberRolePermission> saveMemberRolePermission(MemberRolePermission memberRolePermission) {
        return memberRolePermissionRepo.save(memberRolePermission);
    }
}
