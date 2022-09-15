package com.hanghae.final_project.domain.chatting.service;

import com.hanghae.final_project.domain.chatting.dto.ChatRoomDto;
import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.dto.request.ChatPagingDto;
import com.hanghae.final_project.domain.chatting.dto.response.ResChatPagingDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import com.hanghae.final_project.domain.chatting.repository.ChatRepository;
import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository.CHAT_ROOMS;

@Service
@RequiredArgsConstructor

public class ChatRedisCacheService {

    private final RedisTemplate<String,Object> redisTemplate;

    private final ChatRepository chatRepository;

    private final  WorkSpaceRepository workSpaceRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRedisCacheService.class);

    private HashOperations<String,String,ChatRoomDto> opsHashChatRoom;


    // (마지막 refactoring에는 삭제)
    // Test를 위한 적재해놓는 작업
    //DB에 존재하는 workspace CHAT_ROOM_ID를 redis에 미리 서버 띄울 때, 적재해놓기.
    @PostConstruct
    private void init(){

        opsHashChatRoom=redisTemplate.opsForHash();

        //DB 로부터 workspace_id 가져와서
        List<WorkSpace> workSpaceList = workSpaceRepository.findAll();
        for(WorkSpace workSpace : workSpaceList){

            opsHashChatRoom.put(CHAT_ROOMS,
                    workSpace.getId().toString(),
                    ChatRoomDto.create(workSpace.getId().toString())
            );
        }



    }

    //채팅 메시지가 왔을 때, redis에 적재해놓기
    public void addChat(ChatMessageDto chatMessageDto){

        chatMessageDto.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")));

        redisTemplate.boundSetOps("chat").add(chatMessageDto);

        LOGGER.info("chat data num :"+redisTemplate.boundSetOps("chat").size());

    }

    //WorkspaceId 를 통해서 채팅 내용 조회 Paging 처리
    @Transactional(readOnly = true)
    public ResponseEntity<?> getChats(Long workSpaceId, ChatPagingDto chatPagingDto) {

        if(null==chatPagingDto){
            chatPagingDto= ChatPagingDto.builder()
                    .Cursor(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")))
                    .build();

        }
        Slice<Chat> chatSlice=
        chatRepository
                .findAllByCreatedAtBeforeAndWorkSpace_IdOrderByCreatedAtDesc(
                        chatPagingDto.getCursor(),
                        workSpaceId,
                        PageRequest.of(0,10)
                );
        List<ResChatPagingDto> chats = chatSlice.stream()
                .map(ResChatPagingDto::of)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseDto.success(chats));

    }
}
