package com.hanghae.final_project.domain.chatting.dto.response;

import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResChatPagingDto {

    private Long workSpaceId;
    private String writer;
    private String message;
    private String createdAt;

    public static ResChatPagingDto of(Chat chat){
        return ResChatPagingDto.builder()
                .writer(chat.getUsers())
                .workSpaceId(chat.getWorkSpace().getId())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }

    public static ResChatPagingDto byChatMessageDto(ChatMessageDto chatMessageDto){
        return ResChatPagingDto.builder()
                .writer(chatMessageDto.getWriter())
                .createdAt(chatMessageDto.getCreatedAt())
                .workSpaceId(Long.parseLong(chatMessageDto.getRoomId()))
                .message(chatMessageDto.getMessage())
                .build();
    }
}
