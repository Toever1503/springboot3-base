package com.springboot3base.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommonIdResult{
    @Schema(description = "ID", example = "1")
    private final Long id;
}
