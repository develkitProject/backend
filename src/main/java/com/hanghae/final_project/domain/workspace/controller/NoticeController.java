package com.hanghae.final_project.domain.workspace.controller;


import com.hanghae.final_project.domain.workspace.dto.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.service.NoticeService;
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
    @PostMapping("/api/workspaces/{workspaceid}/notice")
    public ResponseEntity<?> createNotice(@RequestBody NoticeRequestDto noticeRequestDto,
                                          @PathVariable Long workspaceid,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails){
        return noticeService.createNotice(noticeRequestDto,workspaceid,userDetails);
    }

    //공지사항 조회
    @GetMapping("/api/workspaces/{workspaceid}/notice")
    public ResponseEntity<?> getAllNotice(@PathVariable Long workspaceid){
        return noticeService.getAllNotice(workspaceid);
    }

    //공지사항 수정
    @PutMapping("/api/workspaces/{workspaceid}/notice/{noticeId}")
    public ResponseEntity<?> updateNotice(@RequestBody NoticeRequestDto noticeRequestDto,
                                          @PathVariable Long noticeId){
        return noticeService.updateNotice(noticeRequestDto, noticeId);
    }
    //공지사항 삭제
    @DeleteMapping("/api/workspaces/{workspaceid}/notice/{noticeId}")
    public ResponseEntity<?> deleteNotice(@PathVariable Long noticeId){
        return noticeService.deleteNotice(noticeId);
    }

}
