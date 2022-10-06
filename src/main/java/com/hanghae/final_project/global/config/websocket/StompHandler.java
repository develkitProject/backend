package com.hanghae.final_project.global.config.websocket;


import com.hanghae.final_project.api.chat.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.infra.redis.RedisPublisher;
import com.hanghae.final_project.service.chat.ChatRoomService;
import com.hanghae.final_project.global.util.ChatUtils;
import com.hanghae.final_project.global.config.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.config.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
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
            String headerToken = accessor.getFirstNativeHeader(TOKEN);
            String token = headerTokenExtractor.extract(headerToken);
            log.info(jwtDecoder.decodeUsername(token).getUsername());

        }
        // 소켓 연결 후 ,SUBSCRIBE 등록
        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
            log.info("SubScribe destination : " + message.getHeaders().get(SIMP_DESTINATION));
            log.info("SubScribe sessionId : " + message.getHeaders().get(SIMP_SESSION_ID));

            String headerToken = accessor.getFirstNativeHeader(TOKEN);
            String token = headerTokenExtractor.extract(headerToken);
            String username = jwtDecoder.decodeUsername(token).getUsername();

            String destination = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_DESTINATION)
            ).orElse(INVALID_ROOM_ID);

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatUtils.getRoodIdFromDestination(destination);

            //redis에  key(roomId) :  Value( sessionId , nickname ) 저장
            chatRoomService.enterChatRoom(roomId, sessionId, username);


            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.ENTER)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );

        }

        //reids SubScribe 해제
        else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatRoomService.leaveChatRoom(sessionId);

            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.QUIT)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );
        }
        //소켓 연결 후 , 소켓 연결 해제 시
        else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            String sessionId = Optional.ofNullable(
                    (String) message.getHeaders().get(SIMP_SESSION_ID)
            ).orElse(null);

            String roomId = chatRoomService.disconnectWebsocket(sessionId);

            redisPublisher.publish(topic,
                    ChatMessageSaveDto.builder()
                            .type(ChatMessageSaveDto.MessageType.QUIT)
                            .roomId(roomId)
                            .userList(chatRoomService.findUser(roomId, sessionId))
                            .build()
            );

        }
        return message;
    }
}
