package com.hanghae.final_project.domain.repository.workspace;


import com.hanghae.final_project.domain.model.Notice;
import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.WorkSpaceUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface WorkSpaceUserRepository extends JpaRepository<WorkSpaceUser, Long> {

    List<WorkSpaceUser> findAllByUser(User user);

    Optional<WorkSpaceUser> findByUserAndWorkSpaceId(User user, Long workspaceId);

    Optional<WorkSpaceUser> findByUserIdAndWorkSpaceId(Long userId,Long workspaceId);

    Optional<WorkSpaceUser> findByWorkSpaceId(Long workspaceId);

    Slice<WorkSpaceUser> findAllByIdAfterAndWorkSpace_IdOrderByIdAsc(Long cursor, Long workspaceId, Pageable pageable);

    List<WorkSpaceUser> findAllByWorkSpaceId(Long workspaceId);
}