package com.springboot3base.domain.admin;

import com.springboot3base.common.model.response.BaseResponse;
import com.springboot3base.common.model.response.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "03. Admin")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/admin")
public class AdminController {
    private final BaseResponse baseResponse;
    private final AdminService adminService;

    @Operation(summary = "Get logged user's role")
    @GetMapping(value = "/role")
    public CommonResult<List<String>> getRoles() {
        return baseResponse.getContentResult(adminService.getRoles());
    }

}
