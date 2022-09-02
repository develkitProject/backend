package com.hanghae.final_project.domain.workspace.notice.service;


import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.notice.dto.NoticeDto;
import com.hanghae.final_project.domain.workspace.notice.dto.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.notice.repository.NoticeRepository;
import com.hanghae.final_project.domain.workspace.notice.repository.WorkspaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.error.errorcode.CustomErrorCode;
import com.hanghae.final_project.global.error.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class NoticeService {
//db에 연결했으면 직접 갚넣고
    private final NoticeRepository noticeRepository;
    private final WorkspaceRepository workspaceRepository;
    //공지사항 생성
    @Transactional
    public ResponseEntity<?> createNotice(NoticeRequestDto requestDto,Long id, User user) {
        WorkSpace workspace = workspaceRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("공지사항을 찾을 수 없습니다.")
        );
        Notice notice = new Notice(requestDto,user,workspace);
        noticeRepository.save(notice);
        return new ResponseEntity<>(ResponseDto.success(NoticeDto.of(notice)), HttpStatus.OK);
    }
    //공지사항 조회
    @Transactional
    public ResponseEntity<?> getNotice(Long workspaceId) {
        List<Notice> noticeList = noticeRepository.findAllByNotice_IdOrderByCreatedAtDesc(workspaceId);

        List<NoticeDto> noticeDtoList =
                noticeList.stream()
                        .map(NoticeDto::of)
                        .collect(Collectors.toList());
        return new ResponseEntity<>((ResponseDto.success(noticeDtoList)), HttpStatus.OK);
    }


    //공지사항 수정
    @Transactional
    public ResponseEntity<?> updateNotice(NoticeRequestDto requestDto,
                                       Long id, User user){
        Notice notice = noticeRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        if(!user.getUsername().equals(notice.getUser().getUsername()))
            throw new RestApiException(CustomErrorCode.UNAUTHORIZED_TOKEN,"수정 권한이 없습니다.");

        notice.update(requestDto);
        return new ResponseEntity<>(ResponseDto.success(NoticeDto.of(notice)), HttpStatus.OK);
    }

    //공지사항 삭제
    @Transactional
    public ResponseEntity<?> deleteNotice(Long id, User user) {
        Notice notice = noticeRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        if(!user.getUsername().equals(notice.getUser().getUsername()))
            throw new RestApiException(CustomErrorCode.UNAUTHORIZED_TOKEN,"삭제 권한이 없습니다.");
        noticeRepository.delete(notice);
        return new ResponseEntity<>(ResponseDto.success(null), HttpStatus.OK);
    }

}
