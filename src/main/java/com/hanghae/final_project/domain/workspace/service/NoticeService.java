package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.workspace.dto.NoticeResponseDto;
import com.hanghae.final_project.domain.workspace.dto.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.NoticeRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class NoticeService {
    //db에 연결했으면 직접 갚넣고
    private final NoticeRepository noticeRepository;
    private final WorkSpaceRepository workSpaceRepository;

    //공지사항 생성

    @Transactional
    public ResponseEntity<?> createNotice(NoticeRequestDto noticeRequestDto,
                                          Long workSpaceId,
                                          UserDetailsImpl userDetails) {
        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId).orElse(null);
        Notice notice = Notice.builder()
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .user(userDetails.getUser())
                .workSpace(findWorkSpace)
                .build();
        noticeRepository.save(notice);
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .workspaceId(notice.getWorkSpace().getId())
                .nickname(notice.getUser().getNickname())
                .build();

        return new ResponseEntity<>(ResponseDto.success(noticeResponseDto), HttpStatus.OK);
    }

    //공지사항 조회
    @Transactional
    public ResponseEntity<?> getAllNotice(Long workSpaceId) {

        isWorkspaceExist(workSpaceId);

        List<Notice> noticeList = noticeRepository.findAllByWorkSpace_IdOrderByCreatedAtDesc(workSpaceId);
        List<NoticeResponseDto> noticeDtoList =
                noticeList.stream()
                        .map(NoticeResponseDto::of)
                        .collect(Collectors.toList());
        return new ResponseEntity<>(ResponseDto.success(noticeDtoList), HttpStatus.OK);
    }

    //공지사항 수정
    @Transactional
    public ResponseEntity<?> updateNotice(NoticeRequestDto noticeRequestDto,
                                          Long noticeId){
        Notice notice = isNoticeExist(noticeId);
        notice.updateNotice(noticeRequestDto);
        return new ResponseEntity<>(ResponseDto.success(NoticeResponseDto.of(notice)), HttpStatus.OK);
    }


    //공지사항 삭제
    @Transactional
    public ResponseEntity<?> deleteNotice(Long noticeId){
        isNoticeExist(noticeId);
        noticeRepository.deleteById(noticeId);
        return new ResponseEntity<>(ResponseDto.success(null), HttpStatus.OK);
    }
    //workSpcaeId가 없을때 에러코드실행
    private WorkSpace isWorkspaceExist(Long workSpaceId) {
        return workSpaceRepository.findById(workSpaceId).orElseThrow(
                () -> new RequestException(ErrorCode.INVALID_PARAMETER));
    }
    //NoticeId가 없을때 에러코드실행
    private Notice isNoticeExist(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(
                () -> new RequestException(ErrorCode.INVALID_PARAMETER));
    }



    }