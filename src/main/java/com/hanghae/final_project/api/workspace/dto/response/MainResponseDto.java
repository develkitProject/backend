package com.hanghae.final_project.api.workspace.dto.response;

import com.hanghae.final_project.domain.model.User;
import com.hanghae.final_project.domain.model.UserSocialEnum;
import com.hanghae.final_project.domain.model.Document;
import com.hanghae.final_project.domain.model.Notice;
import com.hanghae.final_project.domain.model.WorkSpace;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
public class MainResponseDto {
    private Workspaces workspaces;
    private List<Documents> documents;
    private Notices notices;

    public static MainResponseDto createResponseDto(WorkSpace workSpace, List<Document> documents, Notice notice) {
        List<Documents> documentsList = documents.stream().map(document -> createDocument(document)).collect(Collectors.toList());
        Workspaces workspaces = new Workspaces(workSpace);
        if (notice == null) {
            return new MainResponseDto(workspaces, documentsList, null);
        }
        Notices notices = new Notices(notice);
        return new MainResponseDto(workspaces, documentsList, notices);
    }

    public static Documents createDocument(Document document) {
        Documents documents = new Documents(document);
        return documents;
    }

    @Getter
    @AllArgsConstructor
    static class Documents {
        private Long id;
        private String title;
        private String content;
        private String createdAt;
        private String modifiedAt;
        private Users user;
//        private WorkSpace workSpace;

        public Documents(Document document) {
            this.id = document.getId();
            this.title = document.getTitle();
            this.content = document.getContent();
            this.user = new Users(document.getUser());
            this.createdAt = document.getCreatedAt();
            this.modifiedAt = document.getModifiedAt();
//            this.workSpace = document.getWorkSpace();
        }

        @Getter
        @AllArgsConstructor
        public class Users {
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
    @NoArgsConstructor
    static class Notices {
        private Long id;
        private String title;
        private String content;
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
        }
    }

    @Getter
    @AllArgsConstructor
    static class Workspaces {
        private Long id;
        private String title;
        private String content;
        private String imageUrl;
        private Users createdBy;
        private String invite_code;

        public Workspaces(WorkSpace workSpace) {
            this.id = workSpace.getId();
            this.title = workSpace.getTitle();
            this.content = workSpace.getContent();
            this.imageUrl = workSpace.getImageUrl();
            this.invite_code = workSpace.getInvite_code();
            this.createdBy = new Users(workSpace.getCreatedBy());
        }

        @Getter
        public class Users {
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
}
