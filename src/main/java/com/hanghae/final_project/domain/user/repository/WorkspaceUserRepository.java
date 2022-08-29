package com.hanghae.final_project.domain.user.repository;


import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.model.WorkSpaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface WorkspaceUserRepository extends JpaRepository<WorkSpaceUser, Long> {

    List<WorkSpaceUser> findAllByUser(User user);

}
