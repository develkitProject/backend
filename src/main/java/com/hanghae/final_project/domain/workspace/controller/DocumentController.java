package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentListResponseDto;
import com.hanghae.final_project.domain.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.repository.DocumentQueryRepository;
import com.hanghae.final_project.domain.workspace.service.DocumentService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Document")
@RequiredArgsConstructor
@RestController
public class DocumentController {

    private final DocumentQueryRepository documentQueryRepository;
    private final DocumentService documentService;

    @ApiOperation(value = "문서 생성", notes = "워크스페이스에 따라 구분")
    @PostMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<DocumentResponseDto> createDocument(@PathVariable Long workspaceId,
                                                           @RequestBody DocumentRequestDto documentRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return documentService.createDocument(workspaceId, documentRequestDto, userDetails);
    }

    @ApiOperation(value = "문서 전체 조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<List<DocumentListResponseDto>> getDocumentList(@PathVariable Long workspaceId) {
        return documentService.getDocumentList(workspaceId);
    }

    @ApiOperation(value = "문서 상세 조회", notes = "문서에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<DocumentResponseDto> getDocument(@PathVariable Long workspaceId, @PathVariable Long docId) {
        return documentService.getDocument(workspaceId, docId);
    }


    @ApiOperation(value = "문서 수정", notes = "문서에 따라 구분")
    @PutMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<DocumentResponseDto> updateDocument(@PathVariable Long workspaceId,
                                         @PathVariable Long docId,
                                         @RequestBody DocumentRequestDto documentRequestDto) {
        return documentService.updateDocument(workspaceId, docId, documentRequestDto);
    }


    @ApiOperation(value = "문서 삭제", notes = "문서에 따라 구분")
    @DeleteMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<String> deleteDocument(@PathVariable Long workspaceId,
                                        @PathVariable Long docId) {
        return documentService.deleteDocument(workspaceId, docId);
    }

    @GetMapping("/api/workspaces/{workspaceId}/docs/search")
    public ResponseDto<List<DocumentResponseDto>> searchDocument(@PathVariable Long workspaceId,
                               @RequestParam String type,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String writer){

        return documentQueryRepository.searchDocumentByFilter(workspaceId,type,keyword,writer);
    }

}
