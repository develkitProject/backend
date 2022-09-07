package com.hanghae.final_project.global.exception;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Global Exception")
@RestController
public class ExceptionController {

    @ApiOperation(value = "예외처리", notes = "정해놓은 예외처리를 사")
    @GetMapping("/exception")
    public ResponseEntity<?> exceptionTest() {
        throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
    }
}
