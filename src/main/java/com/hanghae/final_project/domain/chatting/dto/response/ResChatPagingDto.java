package com.hanghae.final_project.domain.chatting.dto.response;

import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
public class ResChatPagingDto {

    private Long workSpaceId;
    private String writer;
    private String message;
    private String createdAt;
    private String nickname;

    public static ResChatPagingDto of(Chat chat){
        return ResChatPagingDto.builder()
                .writer(chat.getUsers())
                .workSpaceId(chat.getWorkSpace().getId())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }

    public static ResChatPagingDto byChatMessageDto(ChatMessageSaveDto chatMessageSaveDto){
        return ResChatPagingDto.builder()
                .writer(chatMessageSaveDto.getWriter())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .workSpaceId(Long.parseLong(chatMessageSaveDto.getRoomId()))
                .message(chatMessageSaveDto.getMessage())
                .build();
    }
}
