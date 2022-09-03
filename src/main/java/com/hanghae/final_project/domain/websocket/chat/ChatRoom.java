package com.hanghae.final_project.domain.websocket.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    private String roomId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private WorkSpace workSpace;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    public static ChatRoom create(WorkSpace workSpace) {
        ChatRoom chatRoom = ChatRoom.builder()
                .workSpace(workSpace)
                .roomId(UUID.randomUUID().toString())
                .users(new ArrayList<>())
                .build();
        return chatRoom;
    }

    public void addUser(User user) {
        users.add(user);
    }
}