package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<WorkSpace, Long> {
}