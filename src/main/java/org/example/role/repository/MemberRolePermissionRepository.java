package org.example.role.repository;

import org.example.role.model.MemberRolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberRolePermissionRepository extends ReactiveCrudRepository<MemberRolePermission,Long> {
    
}
