package com.hanghae.final_project.api.chat.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChatRoomDto implements Serializable {

    private static final long serialVersionUID =  6494678977089006639L;

    private String workSpaceId;
}