package com.hanghae.final_project.global.util.scheduler;

import com.hanghae.final_project.api.chat.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.global.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.hanghae.final_project.domain.repository.chat.ChatRoomRepository.CHAT_SORTED_SET_;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatCachingInRedisScheduling {

    private final ChatUtils chatUtils;
    private final RedisTemplate<String, Object> redisTemplate;

    private final RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void chatCaching() {
        log.info("[Scheduling] redis chat caching start");

        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(CHAT_SORTED_SET_ + "*")
                .build();

        Cursor<String> cursor=redisTemplate.scan(scanOptions);


        //기존 redis caching 데이터 삭제
        while(cursor.hasNext()){
            String matchedKey = cursor.next();
            log.info(matchedKey);
            redisTemplate.delete(matchedKey);
        }

        //redis caching 데이터 1주일치 , 적재하기
        chatUtils.cachingDataToRedisFromDB();

    }
}
