package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.workspace.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByDocId(Long id);
}
