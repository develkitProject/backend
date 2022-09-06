package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserResponseDto {
    private List<User> users = new ArrayList<>();
}
