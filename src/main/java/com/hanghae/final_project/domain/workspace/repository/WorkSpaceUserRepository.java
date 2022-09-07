package com.hanghae.final_project.domain.workspace.repository;


import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.model.WorkSpaceUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WorkSpaceUserRepository extends JpaRepository<WorkSpaceUser, Long> {

    List<WorkSpaceUser> findAllByUser(User user);

    Optional<WorkSpaceUser> findByUserAndWorkSpaceId(User user, Long workspaceId);

    Optional<WorkSpaceUser> findByWorkSpaceId(Long workspaceId);

    List<WorkSpaceUser> findAllByWorkSpaceId(Long workspaceId);
}