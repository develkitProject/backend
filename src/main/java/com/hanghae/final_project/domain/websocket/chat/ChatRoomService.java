package com.hanghae.final_project.domain.websocket.chat;

import com.hanghae.final_project.domain.websocket.redis.RedisSubscriber;
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

    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
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

    //ChannelTopic에 들어갈 때는 roomId가 String
    /*
    * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다*/
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    public List<ChatRoom> findAllRoom() {
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(String id) {
        return chatRoomRepository.findById(id).get();
    }

}