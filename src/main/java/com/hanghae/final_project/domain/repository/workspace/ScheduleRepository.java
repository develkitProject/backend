package com.hanghae.final_project.domain.repository.workspace;


import com.hanghae.final_project.domain.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findAllByWorkSpace_IdOrderByDateDesc(Long id);
    Optional<Schedule> findFirstByWorkSpaceIdOrderByCreatedAtDesc(Long workspaceId);
}
