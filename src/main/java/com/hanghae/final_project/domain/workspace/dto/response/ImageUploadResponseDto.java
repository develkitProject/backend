package com.hanghae.final_project.domain.workspace.dto.response;


import lombok.Builder;
import lombok.Getter;


import java.util.List;

@Builder
@Getter
public class ImageUploadResponseDto {

    private List<String> images;

}
