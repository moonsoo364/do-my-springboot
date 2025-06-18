package org.example.auth.controller;

import lombok.RequiredArgsConstructor;
import org.example.auth.dto.api.DeleteUserDto;
import org.example.auth.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;

    @GetMapping("/me")
    @PreAuthorize("hasAnyAuthority('USER_ROLE', 'ADMIN_ROLE', 'SUPER_ROLE')")
    public Mono<ResponseEntity<String>> getCurrentUser(Mono<Principal> principal) {
        return principal
                .map(Principal::getName)
                .map(name -> ResponseEntity.ok("Hello, " + name + "! You are authenticated."))
                .defaultIfEmpty(ResponseEntity.status(401).body("Unauthorized"));
    }

    @GetMapping("/admin-only")
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE', 'SUPER_ROLE')")
    public Mono<ResponseEntity<String>> adminOnly() {
        return Mono.just(ResponseEntity.ok("This is an admin-only endpoint."));
    }

    @DeleteMapping("/id")
    @PreAuthorize("hasAnyAuthority('ADMIN_ROLE', 'SUPER_ROLE')")
    public Mono<DeleteUserDto> deleteUserId(@RequestParam(required = true) String userId){
        return memberService.deleteByUserId(userId)
                .map(DeleteUserDto::fromDeleteCnt
                );
    }
}
