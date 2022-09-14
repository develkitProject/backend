package com.hanghae.final_project.global.util.image;

import com.hanghae.final_project.domain.workspace.dto.response.ImageUploadResponseDto;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ImageController {

    private final FormDataImageService formDataImageService;

    @PostMapping("/api/images")
    public ResponseEntity<ResponseDto<ImageUploadResponseDto>> upload(
            @RequestParam(value = "image",required = false) MultipartFile[] images) throws IOException {

        return formDataImageService.uploadFiles(images,"image");
    }
}
