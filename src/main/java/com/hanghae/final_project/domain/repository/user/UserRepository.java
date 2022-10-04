package com.hanghae.final_project.domain.repository.user;

import com.hanghae.final_project.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findTopByOrderByIdDesc();


}
