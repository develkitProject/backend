package com.hanghae.final_project.domain.chatting.repository;

import com.hanghae.final_project.domain.chatting.model.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    Slice<Chat> findAllByCreatedAtBeforeAndWorkSpace_IdOrderByCreatedAtDesc(String cursorCreatedAt,Long workspaceId, Pageable pageable);

}
