package com.hanghae.final_project.global.util.image;

import com.hanghae.final_project.domain.workspace.dto.response.ImageUploadResponseDto;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
