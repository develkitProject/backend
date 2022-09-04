package com.hanghae.final_project.domain.chatting.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    private String roomId;
    private String writer;
    private String message;
    private String createdAt;
}
