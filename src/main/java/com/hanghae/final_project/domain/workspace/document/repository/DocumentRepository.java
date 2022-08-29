package com.hanghae.final_project.domain.workspace.document.repository;

import com.hanghae.final_project.domain.workspace.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
