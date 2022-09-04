package com.hanghae.final_project.domain.chatting.service;

import com.hanghae.final_project.domain.chatting.dto.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import com.hanghae.final_project.domain.chatting.repository.ChatRepository;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatCacheService {

    private final RedisTemplate<String,Object> redisTemplate;

    private final RedisTemplate<String,ChatMessageDto> chatRedisTemplate;

    private final ChatRepository chatRepository;

    private final  WorkSpaceRepository workSpaceRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatCacheService.class);
    public void addChat(ChatMessageDto chatMessageDto){

        chatMessageDto.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")));

//        Chat chat = Chat.builder()
//                .users(chatMessageDto.getWriter())
//                .message(chatMessageDto.getMessage())
//                .createdAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS")))
//                .build();

        redisTemplate.boundSetOps("chat").add(chatMessageDto);

        LOGGER.info("chat data num :"+redisTemplate.boundSetOps("chat").size());
        LOGGER.info("chat data num :"+redisTemplate.boundSetOps("chat").getExpire());

    }

    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void writeBack(){
        LOGGER.info("Scheduled ");


        //여기서부터 읽어오는 과정.
        BoundSetOperations<String,ChatMessageDto> setOperations = chatRedisTemplate.boundSetOps("chat");

        ScanOptions scanOptions = ScanOptions.scanOptions().build();

        List<Chat> chatList = new ArrayList<>();
        try( Cursor<ChatMessageDto> cursor= setOperations.scan(scanOptions)){

            while(cursor.hasNext()){
                ChatMessageDto chatMessageDto =cursor.next();
                WorkSpace workSpace= workSpaceRepository
                        .findById(Long.parseLong(chatMessageDto.getRoomId()))
                        .orElse(null);
                chatList.add( Chat.of(chatMessageDto,workSpace));
                LOGGER.info(" chat " +chatMessageDto.getMessage());
            }
            redisTemplate.delete("chat");
            chatRepository.saveAll(chatList);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
