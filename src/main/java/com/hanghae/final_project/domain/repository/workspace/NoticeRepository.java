package com.hanghae.final_project.domain.repository.workspace;


import com.hanghae.final_project.domain.model.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByWorkSpace_IdOrderByCreatedAtDesc(Long id);

    Notice findByWorkSpaceId(Long workSpaceId);

    Optional<Notice> findFirstByWorkSpaceIdOrderByCreatedAtAsc(Long workspaceId);
    Optional<Notice> findFirstByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);

    Slice<Notice> findAllByIdBeforeAndWorkSpace_IdOrderByCreatedAtDesc(Long cursor, Long workspaceId, Pageable pageable);
    Optional<Notice> findAllByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);
}