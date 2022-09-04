package com.hanghae.final_project.domain.chatting.model;

import com.hanghae.final_project.domain.chatting.dto.ChatMessageDto;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Data
@Table(name = "chats")
public class Chat implements Serializable {

    private static final long serialVersionUID = 5090380600159441769L;


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String message;

    @Column
    private String users;

    @Column
    private String createdAt;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

    public static Chat of(ChatMessageDto chatMessageDto,WorkSpace workSpace){
        return Chat.builder()
                .message(chatMessageDto.getMessage())
                .createdAt(chatMessageDto.getCreatedAt())
                .users(chatMessageDto.getWriter())
                .workSpace(workSpace)
                .build();


    }
}
