package com.hanghae.final_project.domain.chatting.controller;

import com.hanghae.final_project.domain.chatting.dto.request.ChatPagingDto;

import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Chat")
@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatDataController {

    private final ChatRedisCacheService cacheService;

    @ApiOperation(value = "채팅", notes = "수정해주세요")
    @PostMapping("/api/chats/{workSpaceId}")
    public ResponseEntity<?> getChatting(@PathVariable Long workSpaceId, @RequestBody(required = false) ChatPagingDto chatPagingDto){

        return cacheService.getChats(workSpaceId,chatPagingDto);
    }
}
