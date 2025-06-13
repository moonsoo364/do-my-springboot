package org.example.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.example.auth.dto.model.MemberDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"password", "regDt", "updDt", "newMember"})
@Table("member") // R2DBC에서 사용하는 Table 매핑
//redis 역직렬화 오류 방지
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements UserDetails, Persistable<String> {

    @Id
    @Column("user_id")
    private String userId;

    @Column("member_name")
    private String memberName;

    @JsonIgnore
    @Column("password")
    private String password;

    @Column("role_code")
    private String roleCode;

    @Column("reg_dt")
    protected LocalDateTime regDt;

    @Column("upd_dt")
    private LocalDateTime updDt;

    @Column("locale_code")
    private String localeCode;

    /** Persistable<String> 관련 코드 시작 */
    @Transient
    private boolean newMember;

    @Override
    public String getId() {
        return this.userId;
    }

    @Override
    public boolean isNew() {
        return this.newMember || userId == null;
    }

    /** Persistable<String> 관련 코드 종료*/

    //repository.save() 생성자
    public Member(MemberDto dto) {
        this.userId = dto.getUserId();
        this.password = dto.getPassword();
        this.memberName = dto.getMemberName();
        this.roleCode = dto.getRoleCode();
        this.localeCode = dto.getLocaleCode();
        this.newMember = dto.isNewMember();// 명시적으로 새로운 행 추가

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(roleCode));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 필요 시 동적으로 변경
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
