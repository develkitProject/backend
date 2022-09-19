package com.hanghae.final_project.domain.chatting.controller;


import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.redis.RedisPublisher;
import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;

import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import com.hanghae.final_project.global.config.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.config.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRedisCacheService chatRedisCacheService;

    private final ChannelTopic channelTopic;
    private final HeaderTokenExtractor headerTokenExtractor;
    private final JwtDecoder jwtDecoder;
    /*
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     * */

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message,@Header("token") String token){
        String nickname = jwtDecoder.decodeUsername(headerTokenExtractor.extract(token));
        message.setWriter(nickname);
        message.setType(ChatMessageDto.MessageType.TALK);
        message.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")));
        redisPublisher.publish(channelTopic,message);
        chatRedisCacheService.addChat(message);
    }
}
