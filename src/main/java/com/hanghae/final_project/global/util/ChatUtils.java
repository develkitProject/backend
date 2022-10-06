package com.hanghae.final_project.global.util;

import com.hanghae.final_project.api.chat.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.domain.model.Chat;
import com.hanghae.final_project.domain.repository.chat.ChatRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hanghae.final_project.domain.repository.chat.ChatRoomRepository.CHAT_SORTED_SET_;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUtils {

    private final ChatRepository chatRepository;

    private final RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate;

    private ZSetOperations<String, ChatMessageSaveDto> zSetOperations;

    //Destination 으로 부터 roomId 값 조회
    public String getRoodIdFromDestination(String destination){
        int lastIndex = destination.lastIndexOf('/');
        if(lastIndex!=-1)
            return destination.substring(lastIndex+1);
        else
            return "";
    }

    //7일전까지의 chat_date redis Insert
    public void cachingDataToRedisFromDB(){

        zSetOperations = chatRedisTemplate.opsForZSet();
        //서버 시작전, redis 에 데이터 적재시키기.
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime cursorDate = current.minusDays(7);

        String cursor = cursorDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        log.info("7일전 날짜 : {}", cursor);

        //7일전 데이터 전부가져와서, redis에 적재
        List<Chat> chatList = chatRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(cursor);

        for (Chat chat : chatList) {
            ChatMessageSaveDto chatMessageSaveDto = ChatMessageSaveDto.of(chat);
            zSetOperations.add(CHAT_SORTED_SET_+chat.getWorkSpace().getId(), chatMessageSaveDto, changeLocalDateTimeToDouble(chat.getCreatedAt()));
        }
    }

    //채팅 데이터 생성일자 Double 형으로 형변환
    public Double changeLocalDateTimeToDouble(String createdAt) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(createdAt, formatter);
        return ((Long) localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).doubleValue();
    }


}
