package com.hanghae.final_project.global.util.image;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3UploaderService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    //Base64 Enocding 된 이미지 S3에 올리기
    public String uploadBase64Image(String file, String dirName) throws IOException { // 파일명, 경로이름
        File uploadFile = convertBase64Image(file)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return uploadBase64Image(uploadFile, dirName);
    }

    //Base64 Encoding 된 이미지 S3에 올리기
    // 로컬에 파일 업로드 하기
    private Optional<File> convertBase64Image(String stringImage) throws IOException {
        byte[] bytes = decodeBase64(stringImage);
        File convertFile = new File(System.getProperty("user.dir") + "/" + "tempFile");
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(bytes);
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }

    //Base64 Encoding 된 이미지 S3에 올리기
    // S3로 파일 업로드하기
    private String uploadBase64Image(File uploadFile, String dirName) {
        String fileName = dirName + "/" + UUID.randomUUID();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    //Formdata로 넘어온 이미지 S3에 올리기
    public ResponseEntity<ResponseDto<ImageUploadResponseDto>> uploadFormDataImage(MultipartFile[] multipartFiles, String dirName) throws IOException {

        if(multipartFiles==null ||multipartFiles[0].getOriginalFilename().equals("")){
            throw new RequestException(ErrorCode.NO_IMAGE_FILE);
        }

        List<File> uploadFiles = convertFormDataImage(multipartFiles)
                .orElseThrow(() -> new RequestException(ErrorCode.COMMON_INTERNAL_ERROR_500));

        return new ResponseEntity<>(
                ResponseDto.success(ImageUploadResponseDto
                        .builder()
                        .images(
                                uploadFiles
                                        .stream()
                                        .map(f -> this.uploadFormDataImage(f, dirName))
                                        .collect(Collectors.toList()))
                        .build()),
                HttpStatus.OK
        );
    }

    //Formdata로 넘어온 이미지 S3에 올리기
    //로컬에 파일 업로드하기
    private Optional<ArrayList<File>> convertFormDataImage(MultipartFile[] files) throws IOException {

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
    private String uploadFormDataImage(File uploadFile, String filepath) {
        String fileName = filepath + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    public void deleteImage(String fileName) {
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
    public void deleteImage(String fileName , String dir){
        //filename이 존재하는지 확인
        if(fileName==null || !fileName.contains("amazonaws.com")) return;
        fileName=fileName.substring(fileName.indexOf(dir));
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    // base64 , Formdata Image 공용
    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // base64 , Formdata Image 공용
    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    public byte[] decodeBase64(String encodedFile) {
        String substring = encodedFile.substring(encodedFile.indexOf(",") + 1);
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(substring);
    }

    // 파일 불러오기
    public String getFileUrl(String path) {
        return amazonS3Client.getUrl(bucket, path).toString();
    }

}
