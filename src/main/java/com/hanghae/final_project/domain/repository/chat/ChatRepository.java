package com.hanghae.final_project.domain.repository.chat;

import com.hanghae.final_project.domain.model.Chat;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat,Long> {

    Slice<Chat> findAllByCreatedAtBeforeAndWorkSpace_IdOrderByCreatedAtDesc(String cursorCreatedAt,Long workspaceId, Pageable pageable);

    List<Chat> findAllByCreatedAtAfterOrderByCreatedAtDesc(String cursorCreatedAt);
}
