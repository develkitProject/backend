package com.hanghae.final_project.domain.chatting.config;

import com.hanghae.final_project.global.config.security.jwt.HeaderTokenExtractor;
import com.hanghae.final_project.global.config.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtDecoder jwtDecoder;

    private final HeaderTokenExtractor headerTokenExtractor;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT == accessor.getCommand()){
                log.info("token 확인");
                log.info("token : "+ accessor.getFirstNativeHeader("token"));
                String headerToken = accessor.getFirstNativeHeader("token");
                String token = headerTokenExtractor.extract(headerToken);;
                log.info(jwtDecoder.decodeUsername(token));
        }
        return message;
    }
}
