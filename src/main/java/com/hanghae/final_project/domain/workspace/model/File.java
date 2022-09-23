package com.hanghae.final_project.domain.workspace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class File {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String fileUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Document doc;
}
