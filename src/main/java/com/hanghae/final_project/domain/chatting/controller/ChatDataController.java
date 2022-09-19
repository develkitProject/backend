package com.hanghae.final_project.domain.chatting.controller;

import com.hanghae.final_project.domain.chatting.dto.request.ChatPagingDto;

import com.hanghae.final_project.domain.chatting.dto.response.ResChatPagingDto;
import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatDataController {

    private final ChatRedisCacheService cacheService;


    @PostMapping("/api/chats/{workSpaceId}")
    public ResponseDto<List<ResChatPagingDto>> getChatting(@PathVariable Long workSpaceId, @RequestBody(required = false) ChatPagingDto chatPagingDto){


        //페이징을 하기위한 Cursor가 parameter로 들어오지 않을 때, 지금 시간을 기준으로 paging
        if(chatPagingDto==null||chatPagingDto.getCursor()==null || chatPagingDto.getCursor().equals("")){
            chatPagingDto= ChatPagingDto.builder()
                    .cursor( LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")))
                    .build();
        }

        return cacheService.getChatsFromRedis(workSpaceId,chatPagingDto);
         //cacheService.getChats(workSpaceId,chatPagingDto.getCursor());
    }
}
