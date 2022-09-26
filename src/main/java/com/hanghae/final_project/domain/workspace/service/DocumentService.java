package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentListResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.DocumentUser;
import com.hanghae.final_project.domain.workspace.model.File;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.*;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import com.hanghae.final_project.global.util.image.S3UploaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class DocumentService {

    private final WorkSpaceRepository workSpaceRepository;
    private final DocumentRepository documentRepository;
    private final S3UploaderService s3UploaderService;
    private final DocumentUserRepository documentUserRepository;
    private final WorkSpaceUserRepository workSpaceUserRepository;
    private final FileRepository fileRepository;

    // 문서 생성
    @Transactional
    public ResponseDto<DocumentResponseDto> createDocument(Long workSpaceId,
                                                           MultipartFile[] multipartFiles,
                                                           DocumentRequestDto documentRequestDto,
                                                           UserDetailsImpl userDetails) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );

        //workspace에 존재하지 않는 유저가 글을 쓸 경우 예외처리
        workSpaceUserRepository
                .findByUserAndWorkSpaceId(userDetails.getUser(), workSpaceId)
                .orElseThrow(() -> new RequestException(ErrorCode.WORKSPACE_IN_USER_NOT_FOUND_404));

        Document document = Document.builder()
                .title(documentRequestDto.getTitle())
                .content(documentRequestDto.getContent())
                .workSpace(findWorkSpace)
                .user(userDetails.getUser())
                .build();

        documentRepository.save(document);

        // S3에 업로드 -> optional로 아무것도 안올릴 때 적용
        List<String> fileUrls;
        try {
            fileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "upload");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<String> filenames = new ArrayList<>();
        List<File> files = new ArrayList<>();
        if(fileUrls != null) {
            for (int i = 0; i < fileUrls.size(); i++) {
                filenames.add(multipartFiles[i].getOriginalFilename());
                files.add(File.builder()
                        .doc(document)
                        .fileName(multipartFiles[i].getOriginalFilename())
                        .fileUrl(fileUrls.get(i))
                        .build());
            }
        }
        fileRepository.saveAll(files);

        DocumentResponseDto documentResponseDto = DocumentResponseDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .nickname(document.getUser().getNickname())
                .fileNames(filenames)
                .fileUrls(fileUrls)
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

    // 문서 상세조회 && 읽음처리 추가진행
    @Transactional
    public ResponseDto<DocumentResponseDto> getDocument(Long workSpaceId, Long id, UserDetailsImpl userDetails) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404)
        );

        //읽음처리 하는 함수 필요
        if(workSpaceUserRepository.findByUserAndWorkSpaceId(userDetails.getUser(),workSpaceId).orElse(null)!=null){
            readDocumentByUser(document, userDetails.getUser());
        }
        else log.info("해당 WorkSpace에 해당하는 회원이 아닙니다. 읽음처리 진행하지 않습니다.");

        //이 문서를 읽은 사람의 정보 list를 불러오기
        List<DocumentUser> documentUserList = documentUserRepository.findAllByDocument(document);

        // 파일 Url 리스트로 가져오기
        List<File> files = fileRepository.findAllByDocId(id);


        DocumentResponseDto documentResponseDto = DocumentResponseDto.builder()
                .id(document.getId())
                .title(document.getTitle())
                .content(document.getContent())
                .nickname(document.getUser().getNickname())
                .fileNames(files
                        .stream()
                        .map(f -> f.getFileName())
                        .collect(Collectors.toList()))
                .fileUrls(files
                        .stream()
                        .map(f -> f.getFileUrl())
                        .collect(Collectors.toList()))
                .workSpaceId(findWorkSpace.getId())
                .createdAt(document.getCreatedAt())
                .readMember(documentUserList
                        .stream()
                        .map(v -> v.getUser().getNickname())
                        .collect(Collectors.toList()))
                .modifiedAt(document.getModifiedAt())
                .build();

        return new ResponseDto<>(true, documentResponseDto, null);

    }

    // 문서 수정
    @Transactional
    public ResponseDto<DocumentResponseDto> updateDocument(Long workSpaceId,
                                                           Long id,
                                                           MultipartFile[] multipartFiles,
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


    // 문서 삭제 -> S3 버켓도 같이 삭제
    //
    @Transactional
    public ResponseDto<String> deleteDocument(Long workSpaceId, Long id) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );


        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404)
        );


        documentRepository.delete(document);

        return new ResponseDto<>(true, "삭제되었습니다.", null);
    }


    public void readDocumentByUser(Document document, User user) {

        //이미 회원이 읽었던 데이터인지 확인.
        DocumentUser documentUser = documentUserRepository
                .findByDocumentAndUser(document, user).orElse(null);


        //읽지 않은 회원이면서 자신이 작성한 글이 아닐 경우 읽음처리 진행.
        if (documentUser == null && !document.getUser().getUsername().equals(user.getUsername())) {

            documentUserRepository
                    .save(
                            DocumentUser.builder()
                                    .document(document)
                                    .user(user)
                                    .build()
                    );
        }

    }

}

/*S3 파일 업로드
생성 -> S3에 올리고, fileRepository.save(String url)
조회 -> fileRepository.findAllByDocId
수정 -> S3에 있는거 삭제하고, 다시 생성
삭제 -> S3 파일, fileRepository 삭제
 */

