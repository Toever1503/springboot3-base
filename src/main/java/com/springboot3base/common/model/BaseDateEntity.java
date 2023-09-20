package com.springboot3base.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Comments;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public class BaseDateEntity {
    @CreationTimestamp
    @Column(name = "create_date", nullable = false)
    @Comments(value = @Comment("time create record at"))
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    @Comments(value = @Comment("time update record at"))
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updateDate;
}