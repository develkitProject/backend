package com.hanghae.final_project.domain.websocket.chat;

import com.hanghae.final_project.domain.websocket.redis.RedisPublisher;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

// import 생략...

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final SimpMessageSendingOperations messageTemplate;

    /*
    * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
    * */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomService.enterChatRoom(message.getRoomId());
            message.setMessage(message.getSender() + "님이 입장하셨습니다.");
        }
        redisPublisher.publish(chatRoomService.getTopic(message.getRoomId()), message);
//        messageTemplate.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }
}