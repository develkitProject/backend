package com.hanghae.final_project.domain.chatting.config;

import com.hanghae.final_project.domain.chatting.redis.RedisPublisher;
import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import com.hanghae.final_project.domain.chatting.utils.ChatUtils;
import com.hanghae.final_project.global.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    public static final String TOKEN = "token";
    public static final String SIMP_DESTINATION = "simpDestination";
    public static final String SIMP_SESSION_ID="simpSessionId";
    public static final String INVALID_ROOM_ID ="InvalidRoomId";

    private final HeaderTokenExtractor headerTokenExtractor;

    private final ChatUtils chatUtils;




    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 최초 소켓 연결
        if(StompCommand.CONNECT == accessor.getCommand()){
                log.info("token 확인");
                log.info("token : "+ accessor.getFirstNativeHeader(TOKEN));
                String headerToken = accessor.getFirstNativeHeader(TOKEN);
                String token = headerTokenExtractor.extract(headerToken);;
                log.info(jwtDecoder.decodeUsername(token));

        }
        // 소켓 연결 후 ,SUBSCRIBE 등록
        else if(StompCommand.SUBSCRIBE==accessor.getCommand()){
            log.info("SubScribe destination : "+ message.getHeaders().get(SIMP_DESTINATION) );
            log.info("SubScribe sessionId : "+message.getHeaders().get(SIMP_SESSION_ID));

            String destination = Optional.ofNullable
                            ((String) message.getHeaders().get(SIMP_DESTINATION)).orElse(INVALID_ROOM_ID);

            //String roomId = chatUtils.getRoodIdFromDestination(destination);
            //redisContainer에 RoodId 를 통해  Channnel Topic등록
            //chatRoomRepository.enterChatRoom(roomId);
        }

        //소켓 연결 후 , 소켓 연결 해제 시
        else if(StompCommand.DISCONNECT == accessor.getCommand()){
            log.info("Disconnect destination : "+ message.getHeaders().get(SIMP_DESTINATION) );
            log.info("Disconnect sessionId : "+message.getHeaders().get(SIMP_SESSION_ID));

            //reids SubScribe 해제
            //Session_Id를 통해서

        }
        return message;
    }
}
