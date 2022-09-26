package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.workspace.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long>{

    List<Document> findAllByWorkSpaceIdOrderByCreatedAtDesc(Long id);

    List<Document> findAllByWorkSpaceId(Long id);

}
