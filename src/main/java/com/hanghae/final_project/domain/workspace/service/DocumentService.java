package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentListResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.DocumentRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceUserRepository;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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

    private final WorkSpaceUserRepository workSpaceUserRepository;

    // 문서 생성
    @Transactional
    public ResponseDto<DocumentResponseDto> createDocument(Long workSpaceId,
                                        DocumentRequestDto documentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );

        //workspace에 존재하지 않는 유저가 글을 쓸 경우 예외처리
        workSpaceUserRepository
                .findByUserAndWorkSpaceId(userDetails.getUser(),workSpaceId)
                .orElseThrow(()->new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404));

        Document document = Document.builder()
                        .title(documentRequestDto.getTitle())
                        .content(documentRequestDto.getContent())
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
    @Transactional(readOnly = true)
    public ResponseDto<List<DocumentListResponseDto>> getDocumentList(Long workSpaceId) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );



        List<Document> documentList = documentRepository.findAllByWorkSpaceIdOrderByCreatedAtDesc(findWorkSpace.getId());
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
    @Transactional(readOnly = true)
    public ResponseDto<DocumentResponseDto> getDocument(Long workSpaceId, Long id) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );


        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404)
        );


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

    // 문서 수정
    @Transactional
    public ResponseDto<DocumentResponseDto> updateDocument(Long workSpaceId,
                                                Long id,
                                                DocumentRequestDto documentRequestDto) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404)
        );


        document.setTitle(documentRequestDto.getTitle());
        document.setContent(documentRequestDto.getContent());

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


    // 문서 삭제
    @Transactional
    public ResponseDto<String> deleteDocument(Long workSpaceId, Long id) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );


        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404)
        );


        documentRepository.delete(document);

        return new ResponseDto<>(true, "삭제되었습니다." , null);
    }

}

