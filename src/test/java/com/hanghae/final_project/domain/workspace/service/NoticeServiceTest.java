package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.dto.request.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.dto.response.NoticeResponseDto;
import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.repository.NoticeRepository;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //가짜 레이어드 환경 생성
public class NoticeServiceTest {

    @InjectMocks
    private NoticeService noticeService;

    @Mock
    private WorkSpaceRepository workSpaceRepository;

    @Mock
    private NoticeResponseDto noticeResponseDto;
    @Mock
    private NoticeRepository noticeRepository;

    public void createNotice() {
        //given
        Long workSpaceId = 1L;
        User user = new User();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        NoticeRequestDto noticeRequestDto = new NoticeRequestDto();
        noticeRequestDto.setTitle("타이틀입니다");
        noticeRequestDto.setContent("컨텐츠입니다.");
        //stub
        when(noticeRepository.save(any())).thenReturn(noticeRequestDto.toEntity());
        //when
        ResponseEntity<ResponseDto<NoticeResponseDto>> noticeResponseDto = noticeService.createNotice(noticeRequestDto, workSpaceId, userDetails);
        //then
        assertThat(noticeResponseDto.getBody().getData().getTitle()).isEqualTo(noticeRequestDto.getTitle());
        assertThat(noticeResponseDto.getBody().getData().getContent()).isEqualTo(noticeRequestDto.getContent());
    }
}


//    @Test
//    public void getAllNotice() {
//        //given
//        Long workspaceId = 1L;
//        User user = new User();
//
//        //stub
//        List<Notice> notices = new ArrayList<>();
//        notices.add(new Notice());
//        //when
//        List<NoticeResponseDto> noticeResponseDtoList = (List<NoticeResponseDto>) noticeService.getAllNotice(workspaceId);
//        //then
//        assertThat(noticeResponseDtoList.get(0).getTitle()).isEqualTo("타이이이트으을!!!");
//        assertThat(noticeResponseDtoList.get(0).getContent()).isEqualTo("content");
//    }
//
//    @Test
//    public void updateNotice() {
//        //given
//        Long id = 1L;
//        Long noticeId = 1L;
//        NoticeRequestDto noticeRequestDto = new NoticeRequestDto();
//        noticeRequestDto.setTitle("kokoko");
//        noticeRequestDto.setContent("bobobo");
//
//        //stub
//        Notice notice = new Notice();
//        Optional<Notice> noticeOptional = Optional.of(notice);
//        when(noticeRepository.findById(id)).thenReturn(noticeOptional);
//
//        //when
//        ResponseEntity<ResponseDto<NoticeResponseDto>> noticeResponseDto = noticeService.updateNotice(noticeRequestDto, noticeId);
//        //then
//        assertThat(noticeResponseDto.getBody().getData().getTitle()).isEqualTo(noticeRequestDto.getTitle());
//        assertThat(noticeResponseDto.getBody().getData().getContent()).isEqualTo(noticeRequestDto.getContent());
//
//    }
//}
