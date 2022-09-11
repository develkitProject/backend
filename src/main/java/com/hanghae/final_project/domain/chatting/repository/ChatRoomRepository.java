package com.hanghae.final_project.domain.chatting.repository;

import com.hanghae.final_project.domain.chatting.dto.ChatRoomDto;
import com.hanghae.final_project.domain.chatting.redis.RedisSubscriber;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Repository
@Getter
public class ChatRoomRepository {

    // 채팅방 topic 에 발행되는 메시지를 처리할 Listener
    private final RedisMessageListenerContainer redisMessageListener;

    //구독처리 서비스
    private final RedisSubscriber redisSubscriber;

    // Redis
    public static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String,Object> redisTemplate;
    private HashOperations<String,String,ChatRoomDto> opsHashChatRoom;

    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보, 서버별로로 매치되는 topic 정보를 Map에 넣어 roomId를 찾을 수 있또록 한다.
    private Map<String, ChannelTopic> topics;

    private final WorkSpaceRepository workSpaceRepository;

//    private Map<String , ChatRoomDto> chatRoomMap;

    @PostConstruct
    private void init(){
        opsHashChatRoom=redisTemplate.opsForHash();
        topics=new HashMap<>();
    }

    //(thymeleaf test용 함수 마지막 refactoring에 제거 예정)
    public List<ChatRoomDto> findAllRoom(){

        return opsHashChatRoom.values(CHAT_ROOMS);

    }
    //(thymeleaf test용 함수 마지막 refactoring에 제거 예정)
    public ChatRoomDto findRoomById(String id){

        return opsHashChatRoom.get(CHAT_ROOMS,id);
    }

    /*
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     * */
    public void enterChatRoom(String roomId){
        ChannelTopic topic = topics.get(roomId);
        if(topic==null){
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber,topic);
            topics.put(roomId,topic);
        }
    }

    public ChannelTopic getTopic(String roomId){
        return topics.get(roomId);
    }
}
