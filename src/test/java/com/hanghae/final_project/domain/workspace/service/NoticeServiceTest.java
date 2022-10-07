package com.hanghae.final_project.domain.workspace.service;

import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.api.workspace.dto.request.NoticeRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.NoticeResponseDto;
import com.hanghae.final_project.domain.repository.workspace.NoticeRepository;
import com.hanghae.final_project.domain.repository.workspace.WorkSpaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.config.security.UserDetailsImpl;
import com.hanghae.final_project.service.workspace.NoticeService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
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
