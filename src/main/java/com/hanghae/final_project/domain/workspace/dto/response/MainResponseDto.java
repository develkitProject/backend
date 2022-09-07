package com.hanghae.final_project.domain.workspace.dto.response;

import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.model.UserSocialEnum;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.domain.workspace.model.Notice;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
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
    private List<Documents> documents;
    private Notices notices;

    public static MainResponseDto createResponseDto(List<Document> documents, Notice notice) {
        List<Documents> documentsList = documents.stream().map(document -> createDocument(document)).collect(Collectors.toList());
        if (notice == null) {
            return new MainResponseDto(documentsList, null);
        }
        Notices notices = new Notices(notice);
        return new MainResponseDto(documentsList, notices);
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
        private Users user;
//        private WorkSpace workSpace;

        public Documents(Document document) {
            this.id = document.getId();
            this.title = document.getTitle();
            this.content = document.getContent();
            this.user = new Users(document.getUser());
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

        public Notices(Notice notice) {
            this.id = notice.getId();
            this.title = notice.getTitle();
            this.content = notice.getContent();
            this.imageUrl = notice.getImageUrl();
        }
    }
}
