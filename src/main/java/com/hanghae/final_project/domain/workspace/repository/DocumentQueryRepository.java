package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.workspace.model.Document;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.hanghae.final_project.domain.workspace.model.QDocument.document;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DocumentQueryRepository {

    public static final String CONTENT_TITLE = "ContentTitle";
    public static final String WRITER = "Writer";
    private final JPAQueryFactory queryFactory;

    public ResponseDto<List<DocumentResponseDto>> searchDocumentByFilter(Long workspaceId,
                                                                         String type,
                                                                         String keyword,
                                                                         String writer) {
        // 제목 & 내용으로 검색하는 경우
        if (type.equals(CONTENT_TITLE)) {

            List<DocumentResponseDto> documentResponseDtos =
                    searchDocumentByKeyword(workspaceId, keyword)
                            .stream()
                            .map(DocumentResponseDto::of)
                            .collect(Collectors.toList());

            return ResponseDto.success(documentResponseDtos);
        }
        // 작성자 이름으로 검색하는 경우
        if (type.equals(WRITER)) {
            List<DocumentResponseDto> documentResponseDtos =
                    searchDocumentByWriter(workspaceId, writer)
                            .stream()
                            .map(DocumentResponseDto::of)
                            .collect(Collectors.toList());

           return  ResponseDto.success(documentResponseDtos);
        }

        throw new RequestException(ErrorCode.SEARCH_TYPE_BAD_REQUEST);
    }

    private List<Document> searchDocumentByKeyword(Long workspaceId, String keyword) {

        log.info("내용 및 제목을 통해 문서 검색 시작");
        log.info("검색어 : {}", keyword);
        return queryFactory
                .selectFrom(document)
                .where(document.title.contains(keyword),
                        document.content.contains(keyword),
                        document.workSpace.id.eq(workspaceId)
                )
                .orderBy(document.createdAt.desc())
                .limit(5)
                .fetch();
    }

    private List<Document> searchDocumentByWriter(Long workspaceId, String writer) {

        log.info("작성자를 통해 문서 검색 시작");
        log.info("작성자 : {}", writer);
        return queryFactory
                .selectFrom(document)
                .where(document.user.nickname.eq(writer),
                        document.workSpace.id.eq(workspaceId))
                .orderBy(document.createdAt.desc())

                .fetch();
    }
    //cursor paging 할 경우,
    private BooleanExpression cursorId(Long cursorId){
        return cursorId == null ? null : document.id.gt(cursorId);

    }

}
