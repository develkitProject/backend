package com.hanghae.final_project.global.exception;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Error handling")
@RestController
public class ExceptionController {

    @ApiOperation(value = "에러코드 반환", notes = "각 기능에 맞는 에러코드 살행")
    @GetMapping("/exception")
    public ResponseEntity<?> exceptionTest() {
        throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
    }
}
