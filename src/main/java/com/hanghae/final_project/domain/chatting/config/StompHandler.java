package com.hanghae.final_project.domain.chatting.config;

import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.redis.RedisPublisher;
import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import com.hanghae.final_project.domain.chatting.service.ChatRedisCacheService;
import com.hanghae.final_project.domain.chatting.service.ChatRoomService;
import com.hanghae.final_project.domain.chatting.utils.ChatUtils;
import com.hanghae.final_project.global.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    public static final String TOKEN = "token";
    public static final String SIMP_DESTINATION = "simpDestination";
    public static final String SIMP_SESSION_ID = "simpSessionId";
    public static final String INVALID_ROOM_ID = "InvalidRoomId";

    private final HeaderTokenExtractor headerTokenExtractor;
    private final ChatUtils chatUtils;

    private final ChannelTopic topic;

    private final ChatRoomService chatRoomService;

    private final RedisPublisher redisPublisher;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // 최초 소켓 연결
        if (StompCommand.CONNECT == accessor.getCommand()) {
            log.info("token 확인");
            log.info("token : " + accessor.getFirstNativeHeader(TOKEN));
            String headerToken = accessor.getFirstNativeHeader(TOKEN);
            String token = headerTokenExtractor.extract(headerToken);
            ;
            log.info(jwtDecoder.decodeUsername(token));

        }
        // 소켓 연결 후 ,SUBSCRIBE 등록
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("SubScribe destination : " + message.getHeaders().get(SIMP_DESTINATION));
            log.info("SubScribe sessionId : " + message.getHeaders().get(SIMP_SESSION_ID));

            String headerToken = accessor.getFirstNativeHeader(TOKEN);
            String token = headerTokenExtractor.extract(headerToken);
            String username = jwtDecoder.decodeUsername(token);

            String destination = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_DESTINATION)
            ).orElse(INVALID_ROOM_ID);

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatUtils.getRoodIdFromDestination(destination);

            //redis에  key(roomId) :  Value( sessionId , nickname ) 저장
            chatRoomService.enterChatRoom(roomId, sessionId, username);

            //list 주기
            redisPublisher.publish(topic,
                    ChatMessageDto.builder()
                            .type(ChatMessageDto.MessageType.ENTER)
                            .roomId(roomId)
                            .message("회원들어옴")
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );

        }
        //소켓 연결 후 , 소켓 연결 해제 시
        else if (StompCommand.DISCONNECT == accessor.getCommand()) {
            log.info("Disconnect destination : " + message.getHeaders().get(SIMP_DESTINATION));
            log.info("Disconnect sessionId : " + message.getHeaders().get(SIMP_SESSION_ID));
            //reids SubScribe 해제
            //Session_Id를 통해서

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatRoomService.leaveChatRoom(sessionId);

            log.info("Socket 연결 끊어진 RoomId : "+roomId);

            //list 주기
            redisPublisher.publish(topic,
                    ChatMessageDto.builder()
                            .type(ChatMessageDto.MessageType.QUIT)
                            .roomId(roomId)
                            .message("회원나감")
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );

        }
        return message;
    }
}
