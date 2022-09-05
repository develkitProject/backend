package com.hanghae.final_project.domain.websocket.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {

    public enum MessageType {
        ENTER, TALK, JOIN
    }

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;


}
