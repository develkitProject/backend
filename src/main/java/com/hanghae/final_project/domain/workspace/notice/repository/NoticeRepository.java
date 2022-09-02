package com.hanghae.final_project.domain.workspace.notice.repository;


import com.hanghae.final_project.domain.workspace.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository <Notice,Long>{

    List<Notice> findAllByNotice_IdOrderByCreatedAtDesc(Long workspaceId);

}
