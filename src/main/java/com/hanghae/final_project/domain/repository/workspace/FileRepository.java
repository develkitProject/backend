package com.hanghae.final_project.domain.repository.workspace;

import com.hanghae.final_project.domain.model.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByDocId(Long id);

    void deleteByFileUrl(String deleteFileUrls);

//    void deleteAllByDocIdAndFileUrl(Long id, List<String> deleteFileUrls);
}
