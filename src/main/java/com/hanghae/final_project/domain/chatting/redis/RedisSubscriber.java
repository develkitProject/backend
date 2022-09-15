package com.hanghae.final_project.domain.chatting.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;


    public void sendMessage(String publishMessage){
        log.info("Subscribe on message() 여기 들어옴");
        try{
            //redis에서 발행된 데이터를 받아 deserialize
            ChatMessageDto roomMessage = objectMapper.readValue(publishMessage,ChatMessageDto.class);


            log.info("writer"+roomMessage.getWriter());
            log.info("roomID"+roomMessage.getRoomId());
            //WebSocket 구독자에게 채팅 메시지 Send
            messagingTemplate.convertAndSend("/sub/chat/room/"+roomMessage.getRoomId(),roomMessage);
        }catch (Exception e){
            log.error(e.getMessage());
        }

    }
}
