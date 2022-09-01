package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.service.DocumentService;
import com.hanghae.final_project.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@RestController
public class DocumentController {

    private final DocumentService documentService;

    // 문서 생성
    @PostMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<?> createDocument(@PathVariable Long workspaceId,
                                         @RequestParam(value = "data") DocumentRequestDto documentRequestDto) {
        return documentService.createDocument(workspaceId, documentRequestDto);
    }

    // 문서 전체조회
    @GetMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<?> getDocumentList(@PathVariable Long workspaceId) {
        return documentService.getDocumentList(workspaceId);
    }

    // 문서 상세조회
    @GetMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<?> getDocument(@PathVariable Long workspaceId, @PathVariable Long docId) {
        return documentService.getDocument(workspaceId, docId);
    }


    // 문서 수정
    @PutMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<?> updateDocument(@PathVariable Long workspaceId,
                                         @PathVariable Long docId,
                                         @RequestParam(value = "data") DocumentRequestDto documentRequestDto,
                                         @RequestPart(value = "file")MultipartFile multipartFile) {
        return documentService.updateDocument(workspaceId, docId, documentRequestDto, multipartFile);
    }


    // 문서 삭제
    @DeleteMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<?> deleteDocument(@PathVariable Long workspaceId,
                                        @PathVariable Long docId) {
        return documentService.deleteDocument(workspaceId, docId);
    }

}

// 리턴타입 제네릭 바꾸기
// 리턴 수정