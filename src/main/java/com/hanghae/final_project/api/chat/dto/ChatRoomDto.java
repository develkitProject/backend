package com.hanghae.final_project.api.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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