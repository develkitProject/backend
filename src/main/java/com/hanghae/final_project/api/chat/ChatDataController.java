package com.hanghae.final_project.api.chat;

import com.hanghae.final_project.api.chat.dto.request.ChatPagingDto;

import com.hanghae.final_project.api.chat.dto.response.ChatPagingResponseDto;
import com.hanghae.final_project.service.chat.ChatRedisCacheService;

import com.hanghae.final_project.global.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Api(tags = "Chat")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatDataController {

    private final ChatRedisCacheService cacheService;

    @ApiOperation(value = "채팅", notes = "채팅 cursor paging 을 통해 조회하기")
    @PostMapping("/api/chats/{workSpaceId}")
    public ResponseDto<List<ChatPagingResponseDto>> getChatting(@PathVariable Long workSpaceId, @RequestBody(required = false) ChatPagingDto chatPagingDto){

        //Cursor 존재하지 않을 경우,현재시간을 기준으로 paging
        if(chatPagingDto==null||chatPagingDto.getCursor()==null || chatPagingDto.getCursor().equals("")){
            chatPagingDto= ChatPagingDto.builder()
                    .cursor( LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")))
                    .build();
        }
        return cacheService.getChatsFromRedis(workSpaceId,chatPagingDto);
    }
}
