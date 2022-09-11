package com.hanghae.final_project.domain.chatting.controller;

import com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository;
import com.hanghae.final_project.global.security.jwt.JwtDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Random;

@RequiredArgsConstructor
@Controller
@Log4j2
public class RoomController {
    private final ChatRoomRepository chatRoomRepository;

    //모든 채팅방 목록 조회
    @GetMapping("/chat/rooms")
    public ModelAndView rooms(){
        log.info("# All Chat Rooms");
        ModelAndView mv =new ModelAndView("chat/rooms");
        mv.addObject("list",chatRoomRepository.findAllRoom());
        return mv;
    }


    //채팅방 조회
    @GetMapping("/chat/room")
    public void getRoom(String roomId, Model model){
        log.info("# get Chat Room , RoomID :"+roomId);
        model.addAttribute("room",chatRoomRepository.findRoomById(roomId));

        model.addAttribute("nickname",(new Random().nextInt(100)+1));
    }
}
