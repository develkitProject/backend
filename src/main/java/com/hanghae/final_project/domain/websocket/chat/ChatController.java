package com.hanghae.final_project.domain.websocket.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

// import 생략...

@Slf4j
@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations messageTemplate;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    /*
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     * */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("token") String token, Authentication authentication) {
        log.info(authentication.getName());
        String nickname = authentication.getName();

        // 로그인 회원 정보로 대화명 설정
        message.setSender(nickname);
        // 채팅방 입장시에는 대화명과 메시지를 자동으로 세팅한다.

        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(nickname + "님이 입장하셨습니다.");
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}