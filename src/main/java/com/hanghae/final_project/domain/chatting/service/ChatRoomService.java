package com.hanghae.final_project.domain.chatting.service;

import com.hanghae.final_project.domain.chatting.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    private final RedisPublisher redisPublisher;

}
