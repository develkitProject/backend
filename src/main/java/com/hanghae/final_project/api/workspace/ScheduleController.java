package com.hanghae.final_project.api.workspace;

import com.hanghae.final_project.api.workspace.dto.request.ScheduleRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.ResSchedulesDto;
import com.hanghae.final_project.service.workspace.ScheduleService;
import com.hanghae.final_project.global.dto.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Schedule")
@RequiredArgsConstructor
@RestController
public class ScheduleController {

    private final ScheduleService scheduleService;

    @ApiOperation(value = "일정 생성", notes = "워크스페이스에 따라 구분")
    @PostMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<ResponseDto<ResSchedulesDto>> registerSchedule(@RequestBody ScheduleRequestDto scheduleRequestDto,
                                                                         @PathVariable Long workspaceId){
        return scheduleService.registerSchedule(scheduleRequestDto,workspaceId);
    }

    @ApiOperation(value = "일정 전체 조회", notes = "워크스페이스에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/schedules")
    public ResponseEntity<ResponseDto<List<ResSchedulesDto>>> getAllSchedules(@PathVariable Long workspaceId){
        return scheduleService.getAllSchedules(workspaceId);
    }

    @ApiOperation(value = "일정 상세 조회", notes = "일정에 따라 구분")
    @GetMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<ResponseDto<ResSchedulesDto>> getSchedules(@PathVariable Long schedulesId){
        return scheduleService.getSchedule(schedulesId);
    }

    @ApiOperation(value = "일정 수정", notes = "일정에 따라 구분")
    @PutMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<ResponseDto<ResSchedulesDto>> updateSchedule(@PathVariable Long schedulesId,
                               @RequestBody ScheduleRequestDto scheduleRequestDto){
        return scheduleService.updateSchedule(schedulesId, scheduleRequestDto);
    }

    @ApiOperation(value = "일정 삭제", notes = "일정에 따라 구분")
    @DeleteMapping("/api/workspaces/{workspaceId}/schedules/{schedulesId}")
    public ResponseEntity<ResponseDto<String>> deleteSchdules(@PathVariable Long schedulesId){
        return scheduleService.deleteSchedules(schedulesId);
    }
}
