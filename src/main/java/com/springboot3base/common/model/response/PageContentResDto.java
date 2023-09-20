package com.springboot3base.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Schema(description = "Page response")
@Data
public class PageContentResDto<T> {
    @Schema(description = "item list")
    private List<T> content = new ArrayList<>();

    @Schema(description = "total record in database")
    private Long totalElements;

    @Schema(description = "is last page or not")
    private boolean last;

    @Schema(description = "total page in database")
    private Integer totalPages;

    @Schema(description = "total elements in content field")
    private Integer numberOfElements;

    public PageContentResDto(Page<T> resDtoPage) {
        for (T userResDto : resDtoPage) {
            this.content.add(userResDto);
        }
        this.totalElements = resDtoPage.getTotalElements();
        this.last = resDtoPage.isLast();
        this.numberOfElements = resDtoPage.getNumberOfElements();
        this.totalPages = resDtoPage.getTotalPages();
    }
}
