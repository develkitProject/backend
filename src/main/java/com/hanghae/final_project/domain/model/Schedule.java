package com.hanghae.final_project.domain.model;

import com.hanghae.final_project.api.workspace.dto.request.ScheduleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Schedule extends Timestamped {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

    public static Schedule of(ScheduleRequestDto scheduleRequestDto, WorkSpace workSpace){
        return Schedule.builder()
                .content(scheduleRequestDto.getContent())
                .date(scheduleRequestDto.getEventDate())
                .workSpace(workSpace)
                .build();
    }

    public void updateSchedule(ScheduleRequestDto scheduleRequestDto){
        this.content= scheduleRequestDto.getContent();
        this.date= scheduleRequestDto.getEventDate();
    }
}
