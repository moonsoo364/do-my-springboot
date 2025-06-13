package org.example.role.controller;

import io.r2dbc.spi.R2dbcDataIntegrityViolationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.consts.ResultCode;
import org.example.common.dto.ApiResponseDto;
import org.example.common.exeption.ApiResponseException;
import org.example.role.dto.model.MemberRoleDto;
import org.example.role.model.MemberRole;
import org.example.role.model.MemberRolePermission;
import org.example.role.service.RoleService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("")
    public Mono<ApiResponseDto> saveMemberRole(@RequestBody @Valid MemberRoleDto memberRole) {
        return roleService.saveMemberRole(new MemberRole(memberRole))
                .hasElement()
                .map(exists -> new ApiResponseDto(ResultCode.SUCCESS, "Success, user role is saved"))
                .onErrorMap(DuplicateKeyException.class,
                        ex -> new ApiResponseException(new ApiResponseDto(ResultCode.BAD_REQUEST, "Fail, user role is duplicated"))
                );
    }




    @PostMapping("/permission")
        public Mono<MemberRolePermission> saveMemberRolePermission(@RequestBody MemberRolePermission memberRolePermission){
            return roleService.saveMemberRolePermission(memberRolePermission);
        }
    }


