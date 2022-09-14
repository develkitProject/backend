package com.hanghae.final_project.global.util.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanghae.final_project.domain.workspace.dto.response.ImageUploadResponseDto;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class FormDataImageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseEntity<ResponseDto<ImageUploadResponseDto>> uploadFiles(MultipartFile[] multipartFiles, String dirName) throws IOException {

        if(multipartFiles==null ||multipartFiles[0].getOriginalFilename().equals("")){
            throw new RequestException(ErrorCode.NO_IMAGE_FILE);
        }

        List<File> uploadFiles = convert(multipartFiles)
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_INTERNAL_ERROR_500));

        return new ResponseEntity<>(
                ResponseDto.success(ImageUploadResponseDto
                        .builder()
                        .images(
                                uploadFiles
                                        .stream()
                                        .map(f -> this.upload(f, dirName))
                                        .collect(Collectors.toList()))
                        .build()),
                HttpStatus.OK
        );
    }

    public String upload(File uploadFile, String filepath) {
        String fileName = filepath + "/" + uploadFile.getName();

        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    //로컬에 삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("Local File delete success");
            return;
        }
        log.info("Local file delete fail");
    }


    //S3 업로드
    public String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    //로컬에 파일 업로드하기
    private Optional<ArrayList<File>> convert(MultipartFile[] files) throws IOException {


        ArrayList<File> fileList = new ArrayList<>();

        //이미지 파일 여러개면 여러개 돌면서 fileList에 담기
        for (MultipartFile file : files) {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File convertFile = new File(System.getProperty("user.dir") + "/" + UUID.randomUUID() + "_" + now + ".png");

            if (convertFile.createNewFile()) {
                try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                    fos.write(file.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RequestException(ErrorCode.COMMON_INTERNAL_ERROR_500);
                }
            }
            fileList.add(convertFile);
        }
        return Optional.of(fileList);


    }
}
