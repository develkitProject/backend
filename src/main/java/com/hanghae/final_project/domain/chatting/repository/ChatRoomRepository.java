package com.hanghae.final_project.domain.chatting.repository;

import com.hanghae.final_project.domain.chatting.dto.ChatRoomDto;
import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import com.hanghae.final_project.domain.chatting.utils.ChatUtils;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@RequiredArgsConstructor
@Repository
@Getter
@Slf4j
public class ChatRoomRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, String> roomRedisTemplate;

    private final WorkSpaceRepository workSpaceRepository;

    private final ChatRedisCacheService chatRedisCacheService;

    private final ChatUtils chatUtils;
    private final RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate;

    private final ChatRepository chatRepository;
    private HashOperations<String, String, String> opsHashEnterRoom;
    private HashOperations<String, String, ChatRoomDto> opsHashChatRoom;
    private BoundHashOperations<String, String, String> setOperations;

    private ListOperations<String, Object> opsListChatData;

    private ZSetOperations<String, ChatMessageSaveDto> zSetOperations;
    public static final String CHAT_ROOMS = "CHAT_ROOM";
    public static final String CHAT_ROOM_ID_ = "CHAT_ROOM_ID_";

    public static final String SESSION_ID = "SESSION_ID";
    public static final String CHAT_SORTED_SET_="CHAT_SORTED_SET_";


    @PostConstruct
    private void init() {
        opsHashEnterRoom = roomRedisTemplate.opsForHash();
        opsHashChatRoom = redisTemplate.opsForHash();
        opsListChatData = redisTemplate.opsForList();
        zSetOperations = chatRedisTemplate.opsForZSet();

        chatUtils.cachingDataToRedisFromDB();
    }

    //(thymeleaf test용 함수 마지막 refactoring에 제거 예정)
    public List<ChatRoomDto> findAllRoom() {

        return opsHashChatRoom.values(CHAT_ROOMS);

    }

    //(thymeleaf test용 함수 마지막 refactoring에 제거 예정)
    public ChatRoomDto findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }


    //채팅 SubScribe 할 때, WebSocket SessionId 를 통해서 redis에 저장
    public void enterChatRoom(String roomId, String sessionId, String username) {

        //세션 - 세션ID - 방 번호
        opsHashEnterRoom.put(SESSION_ID, sessionId, roomId);

        //채팅방 - 세션ID - 유저 아이디
        opsHashEnterRoom.put(CHAT_ROOM_ID_ + roomId, sessionId, username);

    }
    //채팅 DisConnect 할 때, WebSocket SessionId 를 통해서 redis에서 삭제
    public String disconnectWebsocket(String sessionId) {
        String roomId = opsHashEnterRoom.get(SESSION_ID, sessionId);
        opsHashEnterRoom.delete(CHAT_ROOM_ID_ + roomId, sessionId);
        opsHashEnterRoom.delete(SESSION_ID, sessionId);
        return roomId;
    }

    //채팅 unsubscribe 할떄 ,
    public String leaveChatRoom(String sessionId){
        String roomId = opsHashEnterRoom.get(SESSION_ID, sessionId);
        opsHashEnterRoom.delete(CHAT_ROOM_ID_ + roomId, sessionId);
        return roomId;
    }

    public List<String> findUsersInWorkSpace(String roomId, String sessionId) {

        setOperations = roomRedisTemplate.boundHashOps(CHAT_ROOM_ID_ + roomId);
        ScanOptions scanOptions = ScanOptions.scanOptions().build();
        List<String> userListInWorkSpace = new ArrayList<>();

        try (Cursor<Map.Entry<String, String>> cursor = setOperations.scan(scanOptions)) {

            while (cursor.hasNext()) {

                Map.Entry<String, String> data = cursor.next();
                log.info("SessionId : " + data.getKey());
                log.info("Username : " + data.getValue());
                userListInWorkSpace.add(chatRedisCacheService.findUserNicknameByUsername(data.getValue()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return userListInWorkSpace;
    }

//    public Double changeLocalDateTimeToDouble(String createdAt) {
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
//        LocalDateTime localDateTime = LocalDateTime.parse(createdAt, formatter);
//        return ((Long) localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).doubleValue();
//    }



}
