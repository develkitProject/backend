package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.service.DocumentService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "WorkSpace Document")
@RequiredArgsConstructor
@RestController
public class DocumentController {

    private final DocumentService documentService;

    @ApiOperation(value = "문서 생성", notes = "워크스페이스에 따라 구분")
    @PostMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<?> createDocument(@PathVariable Long workspaceId,
                                         @RequestBody DocumentRequestDto documentRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return documentService.createDocument(workspaceId, documentRequestDto, userDetails);
    }

    @ApiOperation(value = "문서 전체조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<?> getDocumentList(@PathVariable Long workspaceId) {
        return documentService.getDocumentList(workspaceId);
    }

    @ApiOperation(value = "문서 상세조회", notes = "문서 아이디로 찾아서 조회")
    @GetMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<?> getDocument(@PathVariable Long workspaceId, @PathVariable Long docId) {
        return documentService.getDocument(workspaceId, docId);
    }


    @ApiOperation(value = "문서 수정", notes = "문서 아이디로 찾아서 수정")
    @PutMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<?> updateDocument(@PathVariable Long workspaceId,
                                         @PathVariable Long docId,
                                         @RequestBody DocumentRequestDto documentRequestDto) {
        return documentService.updateDocument(workspaceId, docId, documentRequestDto);
    }


    @ApiOperation(value = "문서 삭제", notes = "문서 아이디로 찾아서 삭제")
    @DeleteMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<?> deleteDocument(@PathVariable Long workspaceId,
                                        @PathVariable Long docId) {
        return documentService.deleteDocument(workspaceId, docId);
    }

}

// 리턴타입 제네릭 바꾸기
// 리턴 수정