package com.hanghae.final_project.domain.workspace.document.controller;

import com.hanghae.final_project.domain.workspace.document.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class DocumentController {
    private final DocumentService documentService;

    // 문서 생성
    @PostMapping("/api/worksoaces/{workspaceId}/docs")
    public ResponseEntity<?> createDoc(@PathVariable Long workspaceId, @RequestBody DocumentRequestDto docRequestDto) {
        documentService.createDoc(docRequestDto);
        return ResponseEntity.ok(docRequestDto);
    }

    // 문서 전체조회

    // 문서 상세조회

    // 문서 수정

    // 문서 삭제
}
