package com.hanghae.final_project.domain.workspace.notice.controller;


import com.hanghae.final_project.domain.workspace.notice.dto.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.notice.service.NoticeService;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    //공지사항 생성
    @PostMapping("/api/workspace/{workspaceId}/notice")
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequestDto requestDto,
                                          @PathVariable Long Id, //경로
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) { //userDetailseImpl유저정보 ,
        return noticeService.createNotice(requestDto,Id,userDetails.getUser());
    }


    //공지사항 조회
    @GetMapping("/api/workspace/{workspaceId}/notice")
    public ResponseEntity<?> getNotice(@PathVariable Long workspaceId) {
        return noticeService.getNotice(workspaceId);
    }

    //공지사항 수정
    @PutMapping("/api/workspace/{workspaceId}/notice")
    public ResponseEntity<?> updateNotice(@RequestBody NoticeRequestDto requestDto,
                                          @PathVariable Long workspaceId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return noticeService.updateNotice(requestDto,workspaceId,userDetails.getUser());
    }

    //공지사항 삭제
    @DeleteMapping("/api/workspace/{worksapceId}/notice")
    public ResponseEntity<?> deleteNotice(@PathVariable Long workspaceId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails

                                          ) {
        return noticeService.deleteNotice(workspaceId, userDetails.getUser());
    }

}
