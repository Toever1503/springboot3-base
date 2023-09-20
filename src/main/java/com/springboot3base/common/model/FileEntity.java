package com.springboot3base.common.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.Comments;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_file")
public class FileEntity extends BaseDateEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @JdbcTypeCode(SqlTypes.BIGINT)
    private Long id;

    @Column(length = 500, nullable = false)
    @Comments(value = @Comment("WEB URL"))
    private String url;

    @Column(nullable = false)
    @Comments(value = @Comment("path on server or storage"))
    private String path;

    @Column(nullable = false)
    @Comments(value = @Comment("check is verified or not"))
    @ColumnDefault("0")
    private Boolean isVerified;

    public FileEntity(String url, String path) {
        this.url = url;
        this.path = path;
        isVerified = false;
    }

}
