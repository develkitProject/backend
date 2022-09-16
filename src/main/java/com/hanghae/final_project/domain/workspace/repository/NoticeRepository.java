package com.hanghae.final_project.domain.workspace.repository;


import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByWorkSpace_IdOrderByCreatedAtDesc(Long id);

    Notice findByWorkSpaceId(Long workSpaceId);
    Optional<Notice> findFirstByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);

    Optional<Notice> findAllByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);
}