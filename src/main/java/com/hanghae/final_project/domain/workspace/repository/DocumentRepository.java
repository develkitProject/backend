package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    User findByUser(User user);

    Document findByWorkSpaceId(Long workSpaceId);

    List<Document> findAllByWorkSpaceId(Long workSpaceId);


    List<Document> findAllByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);
}
