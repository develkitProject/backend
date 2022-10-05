package com.hanghae.final_project.api.workspace;


import com.hanghae.final_project.api.workspace.dto.request.NoticeRequestDto;
import com.hanghae.final_project.api.workspace.dto.request.PagingRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.NoticeResponseDto;
import com.hanghae.final_project.global.util.annotation.QueryStringArgResolver;
import com.hanghae.final_project.service.workspace.NoticeService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Notice")
@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    @ApiOperation(value = "공지사항 생성", notes = "워크스페이스에 따라 구분")
    @PostMapping("/api/workspaces/{workspaceid}/notice")
    public ResponseEntity<ResponseDto<NoticeResponseDto>> createNotice(@RequestBody NoticeRequestDto noticeRequestDto,
                                                                       @PathVariable Long workspaceid,
                                                                       @AuthenticationPrincipal UserDetailsImpl userDetails){
        return noticeService.createNotice(noticeRequestDto,workspaceid,userDetails);
    }

    @ApiOperation(value = "공지사항 조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceid}/notice")
    public ResponseEntity<ResponseDto<List<NoticeResponseDto>>> getNoticeWithPagination(@PathVariable Long workspaceid,
                                                                                        @QueryStringArgResolver PagingRequestDto requestDto){
        return noticeService.getNoticeWithPagination(workspaceid,requestDto);
    }

    @ApiOperation(value = "공지사항 수정", notes = "공지사항에 따라 구분")
    @PutMapping("/api/workspaces/{workspaceid}/notice/{noticeId}")
    public ResponseEntity<?> updateNotice(@RequestBody NoticeRequestDto noticeRequestDto,
                                          @PathVariable Long noticeId){
        return noticeService.updateNotice(noticeRequestDto, noticeId);
    }

    @ApiOperation(value = "공지사항 삭제", notes = "공지사항에 따라 구분")
    @DeleteMapping("/api/workspaces/{workspaceid}/notice/{noticeId}")
    public ResponseEntity<ResponseDto<String>> deleteNotice(@PathVariable Long noticeId){
        return noticeService.deleteNotice(noticeId);
    }

}
