package com.hanghae.final_project.domain.workspace.model;

import com.hanghae.final_project.domain.workspace.dto.request.ScheduleDto;
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

    public static Schedule of(ScheduleDto scheduleDto,WorkSpace workSpace){
        return Schedule.builder()
                .content(scheduleDto.getContent())
                .date(scheduleDto.getEventDate())
                .workSpace(workSpace)
                .build();
    }

    public void updateSchedule(ScheduleDto scheduleDto){
        this.content=scheduleDto.getContent();
        this.date=scheduleDto.getEventDate();
    }
}
