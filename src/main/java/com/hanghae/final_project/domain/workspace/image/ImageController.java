package com.hanghae.final_project.domain.workspace.image;

import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class ImageController {

    private final FormDataImageService formDataImageService;

    @PostMapping("/api/images")
    public ResponseEntity<ResponseDto> upload(
            @RequestParam(value = "image",required = false) MultipartFile[] images) throws IOException {


        return formDataImageService.uploadFiles(images,"image");
    }
}
