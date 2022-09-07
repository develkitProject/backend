package com.hanghae.final_project.domain.workspace.controller;

import com.hanghae.final_project.domain.workspace.dto.request.ScheduleDto;
import com.hanghae.final_project.domain.workspace.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "WorkSpace Schedule")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "일정 생성", notes = "워크스페이스에 따라 구분")
    @PostMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<?> registerSchedule(@RequestBody ScheduleDto scheduleDto,
                                           @PathVariable Long workspaceId){
        return scheduleService.registerSchedule(scheduleDto,workspaceId);
    }

    @ApiOperation(value = "일정 전체조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<?> getAllSchedules( @PathVariable Long workspaceId){
        return scheduleService.getAllSchedules(workspaceId);
    }

    @ApiOperation(value = "일정 단일조회", notes = "워크스페이스에 따라 구분, schedulesId로 구분")
    @GetMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<?> getSchedules(@PathVariable Long schedulesId){
        return scheduleService.getSchedule(schedulesId);
    }

    @ApiOperation(value = "일정 수정", notes = "워크스페이스에 따라 구분, schedulesId로 구분")
    @PutMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<?> updateSchedule(@PathVariable Long schedulesId,
                               @RequestBody ScheduleDto scheduleDto){
        return scheduleService.updateSchedule(schedulesId,scheduleDto);
    }

    @ApiOperation(value = "일정 삭제", notes = "워크스페이스에 따라 구분, schedulesId로 구분")
    @DeleteMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<?> deleteSchdules(@PathVariable Long schedulesId){
        return scheduleService.deleteSchedules(schedulesId);
    }
}
