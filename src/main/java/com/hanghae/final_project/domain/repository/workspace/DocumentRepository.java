package com.hanghae.final_project.domain.repository.workspace;

import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long>{

    List<Document> findAllByWorkSpaceIdOrderByCreatedAtDesc(Long id);

    Optional<Document> findFirstByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);
    Long countDocumentByUser(User user);
    List<Document> findAllByWorkSpaceId(Long id);

}
