package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentListResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.DocumentRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class DocumentService {

    private final WorkSpaceRepository workSpaceRepository;
    private final DocumentRepository documentRepository;
//    private final S3UploaderService s3UploaderService;

    // 문서 생성
    @Transactional
    public ResponseDto<?> createDocument(Long workSpaceId,
                                        DocumentRequestDto documentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElse(null);
        if(findWorkSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }


//        try {
//            s3UploaderService.upload(documentRequestDto.getImageUrl(), "User");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        Document document = Document.builder()
                        .title(documentRequestDto.getTitle())
                        .content(documentRequestDto.getContent())
//                        .imageUrl(Arrays.toString(s3UploaderService.decodeBase64(documentRequestDto.getImageUrl()))) // 리턴 타입이 byte[]
                        .workSpace(findWorkSpace)
                        .user(userDetails.getUser())
                        .build();

        documentRepository.save(document);

        DocumentResponseDto documentResponseDto = DocumentResponseDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .nickname(document.getUser().getNickname())
                .workSpaceId(findWorkSpace.getId())
                .createdAt(document.getCreatedAt())
                .modifiedAt(document.getModifiedAt())
                .build();

        return new ResponseDto<>(true, documentResponseDto, null);
    }

    // 문서 전체조회
    @Transactional
    public ResponseDto<?> getDocumentList(Long workSpaceId) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElse(null);
        if(findWorkSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        List<Document> documentList = documentRepository.findAllByWorkSpaceId(workSpaceId);
        List<DocumentListResponseDto> documentResponseDtoList = new ArrayList<>();

            for (Document document : documentList) {
                documentResponseDtoList.add(DocumentListResponseDto.builder()
                        .id(document.getId())
                        .title(document.getTitle())
                        .nickname(document.getUser().getNickname())
                        .workSpaceId(workSpaceId)
                        .createdAt(document.getCreatedAt())
                        .modifiedAt(document.getModifiedAt())
                        .build());
            }

        return new ResponseDto<>(true, documentResponseDtoList, null);
    }

    // 문서 상세조회
    @Transactional
    public ResponseDto<?> getDocument(Long workSpaceId, Long id) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElse(null);
        if(findWorkSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        Document document = documentRepository.findById(id).orElse(null);
        if(document == null) {
            throw new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404);
        }
//        document.setWorkSpace(findworkSpace); // 할 팔요 없을 듯

        DocumentResponseDto documentResponseDto = DocumentResponseDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .nickname(document.getUser().getNickname())
                .workSpaceId(workSpaceId)
                .createdAt(document.getCreatedAt())
                .modifiedAt(document.getModifiedAt())
                .build();

        return new ResponseDto<>(true, documentResponseDto, null);

    }

    // 문서 수정 -> 작성자가 아니면 수정 할 수 없도록? 아니면 아무나 수정하고 수정한 사람 이름이 나오게? 그러면 워크스페이스에 있는 사용자인지 확인해야함 이런 개씨발
    @Transactional
    public ResponseDto<?> updateDocument(Long workSpaceId,
                                                Long id,
                                                DocumentRequestDto documentRequestDto) {

        Document document = documentRepository.findById(id).orElse(null);
        if(document == null) {
            throw new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404);
        }

        // 바뀌기 전의 사진파일을 s3에서 삭제
//        s3UploaderService.deleteImage(document.getImageUrl());

        document.setTitle(documentRequestDto.getTitle());
        document.setContent(documentRequestDto.getContent());
//        document.setImageUrl(Arrays.toString(s3UploaderService.decodeBase64(documentRequestDto.getImageUrl())));
//        document.setWorkSpace(workSpace);// 할 필요 없을듯

        documentRepository.save(document);

        DocumentResponseDto documentResponseDto = DocumentResponseDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .nickname(document.getUser().getNickname())
                .workSpaceId(workSpaceId)
                .createdAt(document.getCreatedAt())
                .modifiedAt(document.getModifiedAt())
                .build();

        return new ResponseDto<>(true, documentResponseDto, null);
    }


    // 문서 삭제 -> 작성자가 아니면 삭제할 수 없도록? 아니면 롤을 가진사람이나 본인?
    @Transactional
    public ResponseDto<?> deleteDocument(Long workSpaceId, Long id) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElse(null);
        if(findWorkSpace == null) {
            throw new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404);
        }

        Document document = documentRepository.findById(id).orElse(null);
        if(document == null) {
            throw new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404);
        }

        documentRepository.delete(document);

        return new ResponseDto<>(true, "삭제되었습니다." , null);
    }

}




