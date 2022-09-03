package com.hanghae.final_project.domain.websocket.chat;

import com.hanghae.final_project.domain.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ChatResponseDto {
    private List<User> users = new ArrayList<>();
}
