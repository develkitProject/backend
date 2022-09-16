package com.hanghae.final_project.domain.chatting.service;

import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {

    public final ChatRoomRepository chatRoomRepository;

    public void enterChatRoom(String roomId, String sessionId,String username) {
        chatRoomRepository.enterChatRoom(roomId,sessionId,username);
    }

    public String leaveChatRoom(String sessionId){
        return chatRoomRepository.leaveChatRoom(sessionId);
    }
    public List<String> findUser(String roomId,String sessionId){

       return chatRoomRepository.findUsersInWorkSpace(roomId,sessionId);
    }
}
