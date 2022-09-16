package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.workspace.model.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvitationRepository extends JpaRepository<Invitation,Long> {

    Optional<Invitation> findByWorkSpace_Id(Long workspaceId);
    Optional<Invitation> findByInvite(String invite);
}
