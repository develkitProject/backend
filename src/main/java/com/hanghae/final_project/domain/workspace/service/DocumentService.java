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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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

        // S3에 업로드
        List<String> fileUrls;
        try {
            fileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "upload");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        List<String> filenames = new ArrayList<>();
        List<File> files = new ArrayList<>();
        if (fileUrls != null) {
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
        if (workSpaceUserRepository.findByUserAndWorkSpaceId(userDetails.getUser(), workSpaceId).orElse(null) != null) {
            readDocumentByUser(document, userDetails.getUser());
        } else log.info("해당 WorkSpace에 해당하는 회원이 아닙니다. 읽음처리 진행하지 않습니다.");

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

    // 문서 수정 -> 수정 전의 파일 url들을 받음
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

        // 기존 파일의 url을 받음 -> 멀티파트로 받은 url과 비교해서 없으면 삭제
        List<File> updateFiles = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        List<String> fileUrls = new ArrayList<>();

        List<File> preFiles = fileRepository.findAllByDocId(id);
        List<String> preFileUrlsDB = new ArrayList<>();
        for (File preFile : preFiles) {
            preFileUrlsDB.add(preFile.getFileUrl());
        }

        // 수정 후 파일의 url들과 db에 들어있는 url을 비교해서 없으면 db에서 삭제, 아니면 Dto에 넣어줄 List에 add
        List<String> deleteFileUrls = new ArrayList<>();
        System.out.println(preFileUrlsDB.size());
        if(documentRequestDto.getPreFileUrls() != null) {
            for (int i = 0; i < preFileUrlsDB.size(); i++) {
                if (!documentRequestDto.getPreFileUrls().contains(preFileUrlsDB.get(i))) {
                    System.out.println(preFileUrlsDB.get(i));
                    deleteFileUrls.add(preFileUrlsDB.get(i));
                } else {
                    fileUrls.add(preFiles.get(i).getFileUrl());
                    fileNames.add(preFiles.get(i).getFileName());
                }
            }
        }

        System.out.println(deleteFileUrls.size());
        for(int i = 0; i < deleteFileUrls.size(); i++) {
            System.out.println(deleteFileUrls.get(i));
        }

        for(int i = 0; i < deleteFileUrls.size(); i++) {
            s3UploaderService.deleteImage(deleteFileUrls.get(i), "upload");
            fileRepository.deleteByFileUrl(deleteFileUrls.get(i));
        }

        // 멀티파트 파일로 받은 것들이 추가 된거임 -> s3에 올리고, db에도 저장.
        List<String> updateFileUrls;
        try {
            updateFileUrls = s3UploaderService.uploadFormDataFiles(multipartFiles, "upload");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // updateFiles -> File 타입으로 fileRepository.save에 사용
        // 멀티파트 파일로 받은 것들(추가된것들)을 File 타입으로 dto에 넣어줄 name, url 넣으
        if(multipartFiles != null && multipartFiles[0].getOriginalFilename().equals("") == false) {
            for (int i = 0; i < multipartFiles.length; i++) {
                updateFiles.add(File.builder()
                        .fileUrl(updateFileUrls.get(i))
                        .doc(document)
                        .fileName(multipartFiles[i].getOriginalFilename())
                        .build());
                fileNames.add(multipartFiles[i].getOriginalFilename());
                fileUrls.add(updateFileUrls.get(i));
            }
        }

        fileRepository.saveAll(updateFiles);
        
        // fileNames = 삭제 안된것 + 추가된것들 위에서 다 처리함
        // fileUrls = 삭제 안된것 + 추가된것들 -> 추가된 것들을 넣어줘야함


            document.setTitle(documentRequestDto.getTitle());
            document.setContent(documentRequestDto.getContent());

            documentRepository.save(document);

            DocumentResponseDto documentResponseDto = DocumentResponseDto.builder()
                    .id(document.getId())
                    .title(document.getTitle())
                    .content(document.getContent())
                    .nickname(document.getUser().getNickname())
                    .fileNames(fileNames)
                    .fileUrls(fileUrls)
                    .workSpaceId(findWorkSpace.getId())
                    .createdAt(document.getCreatedAt())
                    .modifiedAt(document.getModifiedAt())
                    .build();

            return new ResponseDto<>(true, documentResponseDto, null);
        }


    // 문서 삭제 -> db삭제, S3 버켓도 같이 삭제
    @Transactional
    public ResponseDto<String> deleteDocument(Long workSpaceId, Long id) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404)
        );

        Document document = documentRepository.findById(id).orElseThrow(
                () -> new RequestException(ErrorCode.DOCUMENT_NOT_FOUND_404)
        );

        List<File> files = fileRepository.findAllByDocId(id);
        List<String> fileUrls = new ArrayList<>();
        for (File file : files) {
            fileUrls.add(file.getFileUrl());
        }

//        for (String fileUrl : fileUrls) {
//            s3UploaderService.deleteImage(fileUrl, "upload");
//        }

        s3UploaderService.deleteFiles(fileUrls);

        fileRepository.deleteAll(files);

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

