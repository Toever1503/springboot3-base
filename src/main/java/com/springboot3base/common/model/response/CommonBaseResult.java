package com.springboot3base.common.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CommonBaseResult {
    @Schema(description = "response code", example = "0")
    private int code;

    @Schema(description = "message about action")
    private String message;
}