package com.hanghae.final_project.domain.workspace.image;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = "Global Image")
@RequiredArgsConstructor
@RestController
public class ImageController {

    private final S3UploaderService s3UploaderService;

    @ApiOperation(value = "이미지 업로드", notes = "S3 버켓 이미지 업로드")
    @PostMapping("/api/images")
    public String upload(@RequestParam("images") String images) throws IOException {
        s3UploaderService.upload(images, "upload");
        return "test";
    }

    @ApiOperation(value = "이미지 삭제", notes = "S3 버컷 이미지 삭제")
    @DeleteMapping("/api/images")
    public String deleteFile(@RequestParam("filename") String filename) {
        System.out.println(filename);
        s3UploaderService.deleteImage(filename);
        return "delete";
    }

}
