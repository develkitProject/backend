package com.hanghae.final_project.domain.workspace.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.model.UserSocialEnum;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.model.WorkSpaceUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class WorkspaceResponseDto {
    private Workspaces workspaces;
/*
    public static WorkspaceResponseDto createResponseDto (WorkSpace workSpace, User user) {
        return new WorkspaceResponseDto(new Workspaces(workSpace, user));
    }*/
    public static WorkspaceResponseDto createResponseDto (WorkSpace workSpace) {
        return new WorkspaceResponseDto(new Workspaces(workSpace));
    }

    @Getter
    @AllArgsConstructor
    static class Workspaces {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private Users createdBy;

        /*public Workspaces(WorkSpace workSpace, User user) {
            this.id = workSpace.getId();
            this.title = workSpace.getTitle();
            this.content = workSpace.getContent();
            this.imageUrl = workSpace.getImageUrl();
            this.createdBy = new Users(user);
        }*/
        public Workspaces(WorkSpace workSpace) {
            this.id = workSpace.getId();
            this.title = workSpace.getTitle();
            this.content = workSpace.getContent();
            this.imageUrl = workSpace.getImageUrl();
            this.createdBy = new Users(workSpace.getCreatedBy());
        }

        @Getter
        @AllArgsConstructor
        static class Users {
            private Long id;
            private String username;
            private String password;
            private String nickname;
            private String profileImage;
            private UserSocialEnum social;

            public Users(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
                this.nickname = user.getNickname();
                this.profileImage = user.getProfileImage();
                this.social = user.getSocial();
            }
        }
    }
}
