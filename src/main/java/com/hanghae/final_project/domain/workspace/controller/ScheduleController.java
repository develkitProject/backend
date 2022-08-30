package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.ScheduleDto;
import com.hanghae.final_project.domain.workspace.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    //schedule 등록
    @PostMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<?> registerSchedule(@RequestBody ScheduleDto scheduleDto,
                                           @PathVariable Long workspaceId){
        return scheduleService.registerSchedule(scheduleDto,workspaceId);
    }

    //schedule 전체조회
    @GetMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<?> getAllSchedules( @PathVariable Long workspaceId){
        return scheduleService.getAllSchedules(workspaceId);
    }

    //schedule 단일조회
    @GetMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<?> getSchedules(@PathVariable Long schedulesId){
        return scheduleService.getSchedule(schedulesId);
    }

    @PutMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long schedulesId,
                               @RequestBody ScheduleDto scheduleDto){
        return scheduleService.updateSchedule(schedulesId,scheduleDto);
    }

    @DeleteMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<?> deleteSchdules(@PathVariable Long schedulesId){
        return scheduleService.deleteSchedules(schedulesId);
    }
}
