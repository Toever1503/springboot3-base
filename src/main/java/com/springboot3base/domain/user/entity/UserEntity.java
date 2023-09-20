package com.springboot3base.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot3base.common.model.BaseDateEntity;
import com.springboot3base.domain.auth.entity.RoleEntity;
import com.springboot3base.domain.user.dto.UserUpdateReqDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Table(name = "tb_user")
public class UserEntity extends BaseDateEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    @Column(name = "name", nullable = false, length = 20)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String name;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "password", length = 128)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String password;

    @Column(name = "tmp_password", length = 128)
    @JdbcTypeCode(SqlTypes.NVARCHAR)
    private String tmpPassword;

    @Column(name = "approved")
    @ColumnDefault("0")
    private Boolean approved;

    @Column(name = "del")
    @ColumnDefault("0")
    private Boolean del;

    @Column(name = "delete_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deleteDate;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleEntity role;

    @Transient
    @Setter
    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return this.username;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setTmpPassword(String password) {
        this.tmpPassword = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void update(UserUpdateReqDto reqDto) {
        if (reqDto.getName() != null) this.name = reqDto.getName();
        if (reqDto.getPhone() != null) this.phone = reqDto.getPhone();
        if (reqDto.getEmail() != null) this.email = reqDto.getEmail();
    }

    public void setDel() {
        this.del = true;
        this.deleteDate = LocalDateTime.now();
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
