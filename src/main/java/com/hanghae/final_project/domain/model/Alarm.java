package com.hanghae.final_project.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "workspace_users_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WorkSpaceUser workspace_user;

    @JoinColumn(name = "workspace_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WorkSpace workspace;

    @JoinColumn(name = "user_id", nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;



}
