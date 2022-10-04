package com.hanghae.final_project.service.workspace;

import com.hanghae.final_project.api.workspace.dto.request.ScheduleRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.ResSchedulesDto;
import com.hanghae.final_project.domain.model.Schedule;
import com.hanghae.final_project.domain.model.WorkSpace;
import com.hanghae.final_project.domain.repository.workspace.ScheduleRepository;
import com.hanghae.final_project.domain.repository.workspace.WorkSpaceRepository;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {

    private final WorkSpaceRepository workSpaceRepository;
    private final ScheduleRepository scheduleRepository;

    public ResponseEntity<ResponseDto<ResSchedulesDto>> registerSchedule(ScheduleRequestDto scheduleRequestDto, Long workspaceId) {

        WorkSpace workSpace = isWorkspaceExist(workspaceId);

        Schedule schedule = Schedule.of(scheduleRequestDto, workSpace);

        scheduleRepository.save(schedule);

        return new  ResponseEntity<>(ResponseDto.success(ResSchedulesDto.of(schedule)), HttpStatus.OK);

    }

    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<List<ResSchedulesDto>>> getAllSchedules(Long workspaceId) {

        isWorkspaceExist(workspaceId);

        List<Schedule> scheduleList =scheduleRepository.findAllByWorkSpace_IdOrderByDateDesc(workspaceId);
        List<ResSchedulesDto> schedulesDtoList =
                scheduleList.stream()
                        .map(ResSchedulesDto::of)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(ResponseDto.success(schedulesDtoList),HttpStatus.OK);
    }
    @Transactional(readOnly = true)
    public ResponseEntity<ResponseDto<ResSchedulesDto>> getSchedule(Long schedulesId) {

        Schedule schedule= isScheduleExist(schedulesId);
       return new ResponseEntity<>(
               ResponseDto.success( ResSchedulesDto.of(schedule)),
               HttpStatus.OK
       );
    }

    public ResponseEntity<ResponseDto<ResSchedulesDto>> updateSchedule(Long schedulesId, ScheduleRequestDto scheduleRequestDto) {
        Schedule schedule= isScheduleExist(schedulesId);
        schedule.updateSchedule(scheduleRequestDto);
        return new ResponseEntity<>(ResponseDto.success(ResSchedulesDto.of(schedule)),HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto<String>> deleteSchedules(Long schedulesId) {
        isScheduleExist(schedulesId);
        scheduleRepository.deleteById(schedulesId);
        return new ResponseEntity<>(ResponseDto.success(null),HttpStatus.OK);
    }


    private WorkSpace isWorkspaceExist(Long workspaceId) {
        return workSpaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RequestException(ErrorCode.WORKSPACE_NOT_FOUND_404));

    }
    private Schedule isScheduleExist(Long schedulesId){
        return scheduleRepository.findById(schedulesId)
                .orElseThrow(()->new RequestException(ErrorCode.SCHEDULE_ID_NOT_FOUND));
    }
}
