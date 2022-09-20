package com.hanghae.final_project.domain.chatting.utils;

import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import com.hanghae.final_project.domain.chatting.repository.ChatRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository.CHAT_SORTED_SET_;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUtils {

    private final ChatRepository chatRepository;

    private final RedisTemplate<String, ChatMessageDto> chatRedisTemplate;

    private ZSetOperations<String, ChatMessageDto> zSetOperations;

    public String getRoodIdFromDestination(String destination){
        int lastIndex = destination.lastIndexOf('/');
        if(lastIndex!=-1)
            return destination.substring(lastIndex+1);
        else
            return "";
    }

    public void cachingDataInRedisFromDB(){

        zSetOperations = chatRedisTemplate.opsForZSet();
        //서버 시작전, redis 에 데이터 적재시키기.
        LocalDateTime current = LocalDateTime.now();//Obtains a LocalDate set to the current system millisecond time using ISOChronology in the default time zone
        LocalDateTime cursorDate = current.minusDays(7);

        String cursor = cursorDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        log.info("7일전 날짜 : {}", cursor);

        //7일전 데이터 전부가져와서, redis에 적재
        List<Chat> chatList = chatRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(cursor);

        for (Chat chat : chatList) {
            ChatMessageDto chatMessageDto = ChatMessageDto.of(chat);
            zSetOperations.add(CHAT_SORTED_SET_+chat.getWorkSpace().getId(), chatMessageDto, changeLocalDateTimeToDouble(chat.getCreatedAt()));
        }
    }
    public Double changeLocalDateTimeToDouble(String createdAt) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(createdAt, formatter);
        return ((Long) localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).doubleValue();
    }
}
