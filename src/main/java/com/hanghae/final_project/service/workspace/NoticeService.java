package com.hanghae.final_project.service.workspace;

import com.hanghae.final_project.api.workspace.dto.request.PagingRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.NoticeResponseDto;
import com.hanghae.final_project.api.workspace.dto.request.NoticeRequestDto;
import com.hanghae.final_project.domain.model.Notice;
import com.hanghae.final_project.domain.model.WorkSpace;
import com.hanghae.final_project.domain.repository.workspace.NoticeRepository;
import com.hanghae.final_project.domain.repository.workspace.WorkSpaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class NoticeService {
    //db에 연결했으면 직접 갚넣고
    private final NoticeRepository noticeRepository;
    private final WorkSpaceRepository workSpaceRepository;

    //공지사항 생성
    @Transactional
    public ResponseEntity<ResponseDto<NoticeResponseDto>> createNotice(NoticeRequestDto noticeRequestDto,
                                                                       Long workSpaceId,
                                                                       UserDetailsImpl userDetails) {

        WorkSpace findWorkSpace = workSpaceRepository.findById(workSpaceId)
                .orElseThrow(() -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404));

        Notice notice = Notice.builder()
                .title(noticeRequestDto.getTitle())
                .content(noticeRequestDto.getContent())
                .user(userDetails.getUser())
                .workSpace(findWorkSpace)
                .build();

        noticeRepository.save(notice);
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.of(notice);

        return new ResponseEntity<>(ResponseDto.success(noticeResponseDto), HttpStatus.OK);
    }

    //공지사항 조회
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<NoticeResponseDto>>> getNoticeWithPagination(Long workSpaceId, PagingRequestDto requestDto) {

        isWorkspaceExist(workSpaceId);
        Long cursor;
        if (requestDto == null) {
            Notice notice = noticeRepository.findFirstByWorkSpaceIdOrderByCreatedAtDesc(workSpaceId)
                    .orElse(Notice.builder().id(1L).build());
            cursor = notice.getId() + 1;
        } else cursor = requestDto.getCursorId();

        Slice<Notice> noticeSlice =
                noticeRepository
                        .findAllByIdBeforeAndWorkSpace_IdOrderByCreatedAtDesc(
                                cursor,
                                workSpaceId,
                                PageRequest.of(0, 5)
                        );


        List<NoticeResponseDto> noticeDtoList =
                noticeSlice.stream()
                        .map(NoticeResponseDto::of)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(ResponseDto.success(noticeDtoList), HttpStatus.OK);
    }

    //공지사항 수정
    @Transactional
    public ResponseEntity<ResponseDto<NoticeResponseDto>> updateNotice(NoticeRequestDto noticeRequestDto,
                                                                       Long noticeId) {
        Notice notice = isNoticeExist(noticeId);
        notice.updateNotice(noticeRequestDto);
        return new ResponseEntity<>(ResponseDto.success(NoticeResponseDto.of(notice)), HttpStatus.OK);
    }


    //공지사항 삭제
    @Transactional
    public ResponseEntity<ResponseDto<String>> deleteNotice(Long noticeId) {
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
