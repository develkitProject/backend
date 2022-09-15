package com.hanghae.final_project.domain.chatting.utils;

import org.springframework.stereotype.Component;

@Component
public class ChatUtils {

    public String getRoodIdFromDestination(String destination){
        int lastIndex = destination.lastIndexOf('/');
        if(lastIndex!=-1)
            return destination.substring(lastIndex+1);
        else
            return "";
    }
}
