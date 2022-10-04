package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.UserSocialEnum;
import com.hanghae.final_project.api.workspace.dto.request.WorkspaceFindRecentData;
import com.hanghae.final_project.domain.model.Notice;
import com.hanghae.final_project.domain.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceResponseDto {

    private Workspaces workspaces;
    private Notices notices;


    // Notice 데이터가 있는 경우 Notice의 데이터를 반환
    public static WorkspaceResponseDto createResponseDto (WorkSpace workSpace, WorkspaceFindRecentData recentData) {
        int size = workSpace.getNotices().size();
        if (size == 0)
            return new WorkspaceResponseDto(new Workspaces(workSpace,recentData), null);
        return new WorkspaceResponseDto(new Workspaces(workSpace,recentData), new Notices(workSpace.getNotices().get(size - 1)));
    }

    @Getter
    @AllArgsConstructor
    static class Workspaces {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private String createdAt;
        private Users createdBy;

        private String documentTitle;
        private String scheduleContent;
        private String documentCreatedAt;
        private String scheduleCreatedAt;



        public Workspaces(WorkSpace workSpace,WorkspaceFindRecentData recentData) {
            this.id = workSpace.getId();
            this.title = workSpace.getTitle();
            this.content = workSpace.getContent();
            this.imageUrl = workSpace.getImageUrl();
            this.createdAt = workSpace.getCreatedAt();
            this.createdBy = new Users(workSpace.getCreatedBy());
            if(recentData!=null){
                this.documentTitle= recentData.getDocumentTitle();
                this.documentCreatedAt=recentData.getDocumentCreatedAt();
                this.scheduleContent=recentData.getScheduleContent();
                this.scheduleCreatedAt=recentData.getScheduleCreatedAt();
            }

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
