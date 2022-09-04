package com.hanghae.final_project.domain.chatting.repository;

import com.hanghae.final_project.domain.chatting.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends JpaRepository<Chat,Long> {
}
