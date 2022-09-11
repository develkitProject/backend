package com.hanghae.final_project.domain.chatting.controller;


import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.redis.RedisPublisher;
import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import com.hanghae.final_project.global.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    private final ChatRedisCacheService chatRedisCacheService;

    private final HeaderTokenExtractor headerTokenExtractor;
    private final JwtDecoder jwtDecoder;
    /*
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     * */
    @MessageMapping("/chat/enter")
    public void enter(ChatMessageDto message, @Header("token") String token) {

        String nickname = jwtDecoder.decodeUsername(headerTokenExtractor.extract(token));

        //message.setWriter(nickname);
        //message.setMessage(nickname+"님이 채팅방에 참여하였습니다");
        chatRoomRepository.enterChatRoom(message.getRoomId());
        log.info( "log Info "+chatRoomRepository.getTopic(message.getRoomId()).toString());
        //redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()),message);

    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageDto message,@Header("token") String token){

        String nickname = jwtDecoder.decodeUsername(headerTokenExtractor.extract(token));

        message.setWriter(nickname);
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()),message);
        chatRedisCacheService.addChat(message);

    }
}
