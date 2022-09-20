package com.hanghae.final_project.domain.chatting.dto.request;

import com.hanghae.final_project.domain.chatting.model.Chat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {

    public enum MessageType{
        ENTER,TALK,QUIT
    }

    private MessageType type;
    private String roomId;
    private String writer;
    private String message;
    private String createdAt;
    private List<String> userList;

    public static ChatMessageDto of (Chat chat){
        return ChatMessageDto.builder()
                .roomId(chat.getWorkSpace().getId().toString())
                .writer(chat.getMessage())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }
}
