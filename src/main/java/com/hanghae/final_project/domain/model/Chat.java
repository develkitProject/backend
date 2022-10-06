package com.hanghae.final_project.domain.model;

import com.hanghae.final_project.api.chat.dto.request.ChatMessageSaveDto;
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

    public static Chat of(ChatMessageSaveDto chatMessageSaveDto, WorkSpace workSpace){

        return Chat.builder()
                .message(chatMessageSaveDto.getMessage())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .users(chatMessageSaveDto.getWriter())
                .workSpace(workSpace)
                .build();


    }
}
