package com.hanghae.final_project.domain.repository.workspace;

import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.Document;
import com.hanghae.final_project.domain.model.DocumentUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentUserRepository extends JpaRepository<DocumentUser,Long> {

    Optional<DocumentUser> findByDocumentAndUser(Document document, User user);

    List<DocumentUser> findAllByUser(User user);
    List<DocumentUser> findAllByDocument(Document document);

}
