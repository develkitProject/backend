package com.hanghae.final_project.api.chat.dto.request;


import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ChatPagingDto {

    private String  message;
    private String  writer;
    private String  cursor;
}
