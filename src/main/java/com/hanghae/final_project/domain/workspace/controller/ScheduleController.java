package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.ScheduleDto;
import com.hanghae.final_project.domain.workspace.dto.response.ResSchedulesDto;
import com.hanghae.final_project.domain.workspace.service.ScheduleService;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    //schedule 등록
    @PostMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<ResponseDto<ResSchedulesDto>> registerSchedule(@RequestBody ScheduleDto scheduleDto,
                                                                         @PathVariable Long workspaceId){
        return scheduleService.registerSchedule(scheduleDto,workspaceId);
    }

    //schedule 전체조회
    @GetMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<ResponseDto<List<ResSchedulesDto>>> getAllSchedules(@PathVariable Long workspaceId){
        return scheduleService.getAllSchedules(workspaceId);
    }

    //schedule 단일조회
    @GetMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<ResponseDto<ResSchedulesDto>> getSchedules(@PathVariable Long schedulesId){
        return scheduleService.getSchedule(schedulesId);
    }

    @PutMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<ResponseDto<ResSchedulesDto>> updateSchedule(@PathVariable Long schedulesId,
                               @RequestBody ScheduleDto scheduleDto){
        return scheduleService.updateSchedule(schedulesId,scheduleDto);
    }

    @DeleteMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<ResponseDto<String>> deleteSchdules(@PathVariable Long schedulesId){
        return scheduleService.deleteSchedules(schedulesId);
    }
}
