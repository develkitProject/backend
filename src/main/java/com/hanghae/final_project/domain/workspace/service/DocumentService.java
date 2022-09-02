package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.DocumentRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
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
    public ResponseDto<Document> createDocument(Long id,
                                                DocumentRequestDto documentRequestDto) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );

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
                        .build();

        documentRepository.save(document);
        return new ResponseDto<>(true, document, null);
    }

    // 문서 전체조회
    @Transactional
    public ResponseDto<List<DocumentResponseDto>> getDocumentList(Long workSpaceId) {
        List<Document> documentList = documentRepository.findByworkSpaceId(workSpaceId);
        List<DocumentResponseDto> documentResponseDtoList = new ArrayList<>();

        for (Document document : documentList) {
            documentResponseDtoList.add(DocumentResponseDto.builder()
                            .id(document.getId())
                            .title(document.getTitle())
                            .content(document.getContent())
                            .build());
        }

        return new ResponseDto<>(true, documentResponseDtoList, null);
    }

    // 문서 상세조회
    @Transactional
    public ResponseDto<Document> getDocument(Long workSpaceId, Long id) {
        WorkSpace findworkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        Document document = documentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 문서입니다.")
        );
//        document.setWorkSpace(findworkSpace); // 할 팔요 없을 듯
        return new ResponseDto<>(true, document, null);

    }

    // 문서 수정
    @Transactional
    public ResponseDto<Document> updateDocument(Long workSpaceId,
                                                Long id,
                                                DocumentRequestDto documentRequestDto) {

        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        Document document = documentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 문서입니다.")
        );

        // 바뀌기 전의 사진파일을 s3에서 삭제
//        s3UploaderService.deleteImage(document.getImageUrl());

        document.setTitle(documentRequestDto.getTitle());
        document.setContent(documentRequestDto.getContent());
//        document.setImageUrl(Arrays.toString(s3UploaderService.decodeBase64(documentRequestDto.getImageUrl())));
        // 할 필요 없을듯
//        document.setWorkSpace(workSpace);

        documentRepository.save(document);

        return new ResponseDto<>(true, document, null);
    }


    // 문서 삭제
    @Transactional
    public ResponseDto<String> deleteDocument(Long workSpaceId, Long id) {
        WorkSpace workSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 워크스페이스입니다.")
        );
        Document document = documentRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 문서입니다.")
        );

        documentRepository.delete(document);

        return new ResponseDto<>(true, "문서가 삭제 되었습니다", null);
    }

}

// static 써야하는이유 -> 생성된 객체를 선언해놓고 써야됨 이해함 ㅇㅋ
// 사진을 어떻게?
// 1. MultipartFile -> 멀티파트파일을 매개변수에 넣어준다.
// 2. URL 스트링으로 -> 인코딩한걸 디코딩 해줘야함 imageService에 있긴함

// 파일을 직접 올릴수도있고 -> multipartFile
// url을 받을 수도 있음 -> 인코딩한거 받아서 디코딩한다.


// s3는 파일 업로드, 삭제, 디코딩 등이있음
// url 디코딩하고 s3에 넣으면 url이 생성이 됨 그걸 리스폰스에 담아서 주면 끝
// 업로드하면 s3 url을 주는거임