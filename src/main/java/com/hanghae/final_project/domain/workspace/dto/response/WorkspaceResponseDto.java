package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.model.UserSocialEnum;
import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WorkspaceResponseDto {
    private Workspaces workspaces;
    private Notices notices;

/*
    public static WorkspaceResponseDto createResponseDto (WorkSpace workSpace, User user) {
        return new WorkspaceResponseDto(new Workspaces(workSpace, user));
    }*/
    // Notice 데이터가 있는 경우 Notice의 데이터를 반환
    public static WorkspaceResponseDto createResponseDto (WorkSpace workSpace) {
        int size = workSpace.getNotices().size();
        if (size == 0)
            return new WorkspaceResponseDto(new Workspaces(workSpace), null);
        return new WorkspaceResponseDto(new Workspaces(workSpace), new Notices(workSpace.getNotices().get(size - 1)));
    }

    @Getter
    @AllArgsConstructor
    static class Workspaces {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private String invite_code;
        private String createdAt;
        private Users createdBy;



        public Workspaces(WorkSpace workSpace) {
            this.id = workSpace.getId();
            this.title = workSpace.getTitle();
            this.content = workSpace.getContent();
            this.imageUrl = workSpace.getImageUrl();
            this.invite_code = workSpace.getInvite_code();
            this.createdAt = workSpace.getCreatedAt();
            this.createdBy = new Users(workSpace.getCreatedBy());
        }

        @Getter
        @AllArgsConstructor
        static class Users {
            private Long id;
            private String username;
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

    @Getter
    @AllArgsConstructor
    static class Notices {

        private Long id;
        private String title;
        private String content;
        private Users users;
        private String imageUrl;
        private String createdAt;
        private String modifiedAt;

        public Notices(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.imageUrl = notice.getImageUrl();
            this.createdAt = notice.getCreatedAt();
            this.modifiedAt = notice.getModifiedAt();
            this.users = new Users(notice.getUser());
        }

        @Getter
        @AllArgsConstructor
        static class Users {
            private Long id;
            private String username;
            private String nickname;
            private String profileImage;

            public Users(User user) {
                this.id = user.getId();
                this.username = user.getUsername();
                this.nickname = user.getNickname();
                this.profileImage = user.getProfileImage();
            }
        }
    }
}
