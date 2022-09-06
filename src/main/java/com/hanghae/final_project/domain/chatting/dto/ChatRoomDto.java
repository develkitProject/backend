package com.hanghae.final_project.domain.chatting.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomDto implements Serializable {

    private static final long serialVersionUID =  6494678977089006639L;

    private String workSpaceId;

    public static ChatRoomDto create(String workSpaceId) {
        ChatRoomDto room = new ChatRoomDto();
        //room.roomId = UUID.randomUUID().toString();
        room.workSpaceId = workSpaceId;
        return room;
    }
}