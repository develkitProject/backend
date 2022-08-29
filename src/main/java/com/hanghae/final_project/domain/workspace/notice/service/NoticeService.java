package com.hanghae.final_project.domain.workspace.notice.service;


import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.notice.dto.NoticeRequestDto;
import com.hanghae.final_project.domain.workspace.notice.dto.NoticeResponseDto;
import com.hanghae.final_project.domain.workspace.notice.repository.NoticeRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RequiredArgsConstructor
@Service
public class NoticeService {

    private final NoticeRepository noticeRepository;

    //공지사항 생성
    @Transactional
    public ResponseDto<?> createNotice(NoticeRequestDto requestDto, Long id) {
        Notice notice = new Notice(requestDto,id);
        noticeRepository.save(notice);
        return ResponseDto.success("");
    }
}
