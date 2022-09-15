package com.hanghae.final_project.domain.chatting.utils;

import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import com.hanghae.final_project.domain.chatting.repository.ChatRepository;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWriteBackScheduling {

    private final RedisTemplate<String,Object> redisTemplate;

    private final RedisTemplate<String,ChatMessageDto> chatRedisTemplate;

    private final ChatRepository chatRepository;
    private final WorkSpaceRepository workSpaceRepository;

    //@Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void writeBack(){
        log.info("Scheduling start");
        //여기서부터 읽어오는 과정.
        BoundSetOperations<String, ChatMessageDto> setOperations = chatRedisTemplate.boundSetOps("chat");

        ScanOptions scanOptions = ScanOptions.scanOptions().build();

        List<Chat> chatList = new ArrayList<>();
        try( Cursor<ChatMessageDto> cursor= setOperations.scan(scanOptions)){

            while(cursor.hasNext()){
                ChatMessageDto chatMessageDto =cursor.next();
                WorkSpace workSpace= workSpaceRepository
                        .findById(Long.parseLong(chatMessageDto.getRoomId()))
                        .orElse(null);
                chatList.add( Chat.of(chatMessageDto,workSpace));
                log.info(" chat " +chatMessageDto.getMessage());
            }
            redisTemplate.delete("chat");
            chatRepository.saveAll(chatList);

        }catch (Exception e){
            e.printStackTrace();
        }

        log.info("Scheduling done");
    }
}
