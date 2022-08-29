package com.hanghae.final_project.domain.workspace.notice.controller;


import com.hanghae.final_project.domain.workspace.notice.dto.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.notice.service.NoticeService;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 생성
    @PostMapping("/api/workspace/{workspaceId}/notice")
    public ResponseDto<?> createNotice(@RequestBody NoticeRequestDto requestDto,
                                       @PathVariable Long id){
        return noticeService.createNotice(requestDto,id);
    }

}
