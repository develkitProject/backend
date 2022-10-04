package com.hanghae.final_project.api.chat.dto.response;

import com.hanghae.final_project.api.chat.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.domain.model.Chat;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
public class ChatPagingResponseDto {

    private Long workSpaceId;
    private String writer;
    private String message;
    private String createdAt;
    private String nickname;

    public static ChatPagingResponseDto of(Chat chat){
        return ChatPagingResponseDto.builder()
                .writer(chat.getUsers())
                .workSpaceId(chat.getWorkSpace().getId())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }

    public static ChatPagingResponseDto byChatMessageDto(ChatMessageSaveDto chatMessageSaveDto){
        return ChatPagingResponseDto.builder()
                .writer(chatMessageSaveDto.getWriter())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .workSpaceId(Long.parseLong(chatMessageSaveDto.getRoomId()))
                .message(chatMessageSaveDto.getMessage())
                .build();
    }
}
