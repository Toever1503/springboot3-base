package com.springboot3base.domain.auth.entity;

import com.springboot3base.common.model.BaseDateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_authentication")
@Builder
public class AuthenticationEntity extends BaseDateEntity {
    @Id
    @Column(name = "id", nullable = false, length = 128)
    private String id;

    @Column(name = "user_id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    @Comments(value = @Comment("User id"))
    private Long userId;

    @Column(name = "access_token", nullable = false, length = 256)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, length = 256)
    private String refreshToken;

    @Column(name = "has_revoked", nullable = false)
    @Comments(value = @Comment("mark token has revoked"))
    @ColumnDefault("0")
    private Boolean hasRevoked;

    public void setLogout() {
        this.hasRevoked = true;
    }
}
