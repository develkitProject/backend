package com.hanghae.final_project.api.workspace.dto.response;


import lombok.Builder;
import lombok.Getter;


import java.util.List;

@Builder
@Getter
public class ImageUploadResponseDto {

    private List<String> images;


    public static ImageUploadResponseDto of(List<String> imagesList){
       return  ImageUploadResponseDto.builder()
               .images(imagesList)
               .build();
    }
}
