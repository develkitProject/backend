package com.hanghae.final_project.domain.user.repository;


import com.hanghae.final_project.domain.workspace.model.WorkSpaceUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceUserRepository extends JpaRepository<WorkSpaceUser, Long> {

}
