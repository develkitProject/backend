package com.hanghae.final_project.domain.chatting.redis;

import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {

    /*
     * redis 발행 서비스
     *
     * */
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageDto messageDto) {


        redisTemplate.convertAndSend(topic.getTopic(), messageDto);
    }

    public void publishRoomInfo(ChannelTopic topic) {
        redisTemplate.convertAndSend(topic.getTopic(), "");
    }
}