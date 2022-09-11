package com.hanghae.final_project.domain.chatting.repository;

import com.hanghae.final_project.domain.chatting.dto.ChatRoomDto;
import com.hanghae.final_project.domain.chatting.redis.RedisSubscriber;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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

    private final RedisTemplate<String,Object> redisTemplate;
    private HashOperations<String,String,String> opsHashEnterRoom;
    private HashOperations<String,String,ChatRoomDto> opsHashChatRoom;

    private final WorkSpaceRepository workSpaceRepository;

    public static final String CHAT_ROOMS = "CHAT_ROOM";
    public static final String CHAT_ROOM_ID_="CHAT_ROOM_ID_";

    public static final String SESSION_ID="SESSION_ID";

    @PostConstruct
    private void init(){

        opsHashEnterRoom=redisTemplate.opsForHash();
        opsHashChatRoom=redisTemplate.opsForHash();

    }

    //(thymeleaf test용 함수 마지막 refactoring에 제거 예정)
    public List<ChatRoomDto> findAllRoom(){

        return opsHashChatRoom.values(CHAT_ROOMS);

    }
    //(thymeleaf test용 함수 마지막 refactoring에 제거 예정)
    public ChatRoomDto findRoomById(String id){
        return opsHashChatRoom.get(CHAT_ROOMS,id);
    }


    //채팅 SubScribe 할 때, WebSocket SessionId 를 통해서 redis에 저장
    public void enterChatRoom(String roomId,String sessionId,String username){

        //세션 - 세션ID - 방 번호
        opsHashEnterRoom.put(SESSION_ID,sessionId,roomId);

        //채팅방 - 세션ID - 유저 아이디
        opsHashEnterRoom.put(CHAT_ROOM_ID_+roomId,sessionId,username);


    }

    //채팅 DisConnect 할 때, WebSocket SessionId 를 통해서 redis에서 삭제
    public void leaveChatRoom(String sessionId) {
        String roomId=opsHashEnterRoom.get(SESSION_ID,sessionId);
        opsHashEnterRoom.delete(SESSION_ID,sessionId);
        opsHashEnterRoom.delete(CHAT_ROOM_ID_+roomId,sessionId);
    }
}
