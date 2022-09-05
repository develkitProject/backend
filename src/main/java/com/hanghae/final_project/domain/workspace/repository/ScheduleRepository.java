package com.hanghae.final_project.domain.workspace.repository;


import com.hanghae.final_project.domain.workspace.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findAllByWorkSpace_IdOrderByDateDesc(Long id);
}
