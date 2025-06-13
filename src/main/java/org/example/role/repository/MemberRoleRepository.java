package org.example.role.repository;

import org.example.role.model.MemberRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MemberRoleRepository extends ReactiveCrudRepository<MemberRole,String> {

}
