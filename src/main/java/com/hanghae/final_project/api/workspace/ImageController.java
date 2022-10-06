package com.hanghae.final_project.api.workspace;

import com.hanghae.final_project.api.workspace.dto.response.ImageUploadResponseDto;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.infra.s3fileupload.S3UploaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Api(tags = "Image")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ImageController {

    private final S3UploaderService s3UploaderService;

    @ApiOperation(value = "이미지 저장", notes = "S3 버켓에 저장")
    @PostMapping("/api/images")
    public ResponseDto<ImageUploadResponseDto> upload(
            @RequestParam(value = "image",required = false) MultipartFile[] images) throws IOException {

        return ResponseDto.success(ImageUploadResponseDto.of(s3UploaderService.uploadFormDataFiles(images,"image")));

    }
}
