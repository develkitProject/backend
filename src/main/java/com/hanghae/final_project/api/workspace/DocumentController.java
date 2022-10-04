package com.hanghae.final_project.api.workspace;

import com.hanghae.final_project.api.workspace.dto.request.DocumentRequestDto;
import com.hanghae.final_project.api.workspace.dto.request.PagingRequestDto;
import com.hanghae.final_project.api.workspace.dto.request.SearchDocumentRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.repository.workspace.DocumentQueryRepository;
import com.hanghae.final_project.service.workspace.DocumentService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import com.hanghae.final_project.global.util.annotation.QueryStringArgResolver;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
                                                           @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles,
                                                           @RequestPart(value = "data") DocumentRequestDto documentRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return documentService.createDocument(workspaceId, multipartFiles, documentRequestDto, userDetails);
    }

    @ApiOperation(value = "문서 전체 조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/docs")
    public ResponseDto<List<DocumentResponseDto>> getDocumentList(@PathVariable Long workspaceId,
                                                                      @QueryStringArgResolver PagingRequestDto requestDto) {


        return documentQueryRepository.getDocumentWithPaging(workspaceId,requestDto);
         // documentService.getDocumentList(workspaceId,requestDto);
    }

    @ApiOperation(value = "문서 상세 조회", notes = "문서에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<DocumentResponseDto> getDocument(@PathVariable Long workspaceId,
                                                        @PathVariable Long docId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return documentService.getDocument(workspaceId, docId,userDetails);
    }


    @ApiOperation(value = "문서 수정", notes = "문서에 따라 구분")
    @PutMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<DocumentResponseDto> updateDocument(@PathVariable Long workspaceId,
                                                           @PathVariable Long docId,
                                                           @RequestPart(value = "files", required = false) MultipartFile[] multipartFiles,
                                                           @RequestPart(value = "data") DocumentRequestDto documentRequestDto,
                                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return documentService.updateDocument(workspaceId, docId, multipartFiles, documentRequestDto,userDetails.getUser());
    }


    @ApiOperation(value = "문서 삭제", notes = "문서에 따라 구분")
    @DeleteMapping("/api/workspaces/{workspaceId}/docs/{docId}")
    public ResponseDto<String> deleteDocument(@PathVariable Long workspaceId,
                                              @PathVariable Long docId) {
        return documentService.deleteDocument(workspaceId, docId);
    }

    @ApiOperation(value="문서 검색", notes = "문서 작성자 or (내용+제목) 으로 검색 가능")
    @GetMapping("/api/workspaces/{workspaceId}/docs/search")
    public ResponseDto<List<DocumentResponseDto>> searchDocument(@PathVariable Long workspaceId,
                               @QueryStringArgResolver SearchDocumentRequestDto requestDto) {

        return documentQueryRepository.searchDocumentByFilter(workspaceId, requestDto);
    }

}
