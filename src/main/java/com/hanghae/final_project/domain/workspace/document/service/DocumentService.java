package com.hanghae.final_project.domain.workspace.document.service;

import com.hanghae.final_project.domain.user.image.AmazonS3Config;
import com.hanghae.final_project.domain.user.image.S3UploaderService;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.WorkSpace;
import com.hanghae.final_project.domain.workspace.document.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.document.model.Document;
import com.hanghae.final_project.domain.workspace.document.repository.DocumentRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;

@RequiredArgsConstructor
@Service
public class DocumentService {
    private final DocumentRepository documentRepository;


    // 문서 생성
//    @Transactional
//    public ResponseDto<Document> createDoc(DocumentRequestDto documentRequestDto,
//                                           MultipartFile multipartFile, Principal principal) throws IOException {
//        WorkSpace workSpace = documentRepository.findById(id);
//        AmazonS3Config image = S3UploaderService.upload(multipartFile, "static");
//
//        String imageUrl = amazonS3Domain + URLEncoder.encode(image.getKey(), StandardCharsets.US_ASCII);
//
//        documentRequestDto.setImageUrl(imageUrl);
//        Document document = new Document(documentRequestDto);
//        return ResponseDto.success(documentRepository.save(document));
}
