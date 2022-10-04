package com.hanghae.final_project.domain.model;

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
public class Invitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invite;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkSpace workSpace;

    public static Invitation of(String invitationCode,WorkSpace workSpace){
        return Invitation.builder()
                .invite(invitationCode)
                .workSpace(workSpace)
                .build();
    }
}
