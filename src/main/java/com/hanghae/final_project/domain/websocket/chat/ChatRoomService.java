package com.hanghae.final_project.domain.websocket.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// import 생략....

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    // 채팅방에 발행되는 메시지를 처리할 listener
    private final RedisMessageListenerContainer redisMessageListener;

    private final ChatRoomRepository chatRoomRepository;

    //Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private Map<String, ChannelTopic> topics;

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }

    /*
    * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다
    * */
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom;
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(String id) {
        return chatRoomRepository.findById(id).get();
    }

}