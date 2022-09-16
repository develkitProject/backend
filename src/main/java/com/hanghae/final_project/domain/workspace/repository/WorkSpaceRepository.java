package com.hanghae.final_project.domain.workspace.repository;



import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkSpaceRepository extends JpaRepository<WorkSpace,Long> {

}

