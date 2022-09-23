package com.hanghae.final_project.domain.workspace.repository;

import com.hanghae.final_project.domain.workspace.dto.request.SearchDocumentRequestDto;
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

    public static final String PREVIOUS = "Previous";

    public static final String RECENT = "Recent";
    private final JPAQueryFactory queryFactory;

    public ResponseDto<List<DocumentResponseDto>> searchDocumentByFilter(Long workspaceId,
                                                                         SearchDocumentRequestDto requestDto) {

        // 제목 & 내용으로 검색하는 경우
        if (requestDto.getType().equals(CONTENT_TITLE)) {

            List<DocumentResponseDto> documentResponseDtos =
                    searchDocumentByKeyword(workspaceId,requestDto)
                            .stream()
                            .map(DocumentResponseDto::of)
                            .collect(Collectors.toList());

            return ResponseDto.success(documentResponseDtos);
        }
        // 작성자 이름으로 검색하는 경우
        if (requestDto.getType().equals(WRITER)) {
            List<DocumentResponseDto> documentResponseDtos =
                    searchDocumentByWriter(workspaceId, requestDto)
                            .stream()
                            .map(DocumentResponseDto::of)
                            .collect(Collectors.toList());

            return ResponseDto.success(documentResponseDtos);
        }

        throw new RequestException(ErrorCode.SEARCH_TYPE_BAD_REQUEST);
    }

    private List<Document> searchDocumentByKeyword(Long workspaceId, SearchDocumentRequestDto requestDto) {

        log.info("내용 및 제목을 통해 문서 검색 시작");
        log.info("검색어 : {}", requestDto.getKeyword());
        return queryFactory
                .selectFrom(document)
                .where(document.title.contains(requestDto.getKeyword()),
                        document.content.contains(requestDto.getKeyword()),
                        document.workSpace.id.eq(workspaceId),
                        cursorIdControl(requestDto.getCursorId(), requestDto.getDirection())
                        )
                .orderBy(document.createdAt.desc())
                .limit(5)
                .fetch();
    }

    private List<Document> searchDocumentByWriter(Long workspaceId, SearchDocumentRequestDto requestDto) {

        log.info("작성자를 통해 문서 검색 시작");
        log.info("작성자 : {}", requestDto.getWriter());
        return queryFactory
                .selectFrom(document)
                .where(document.user.nickname.eq(requestDto.getWriter()),
                        document.workSpace.id.eq(workspaceId),
                        cursorIdControl(requestDto.getCursorId(), requestDto.getDirection()))
                .orderBy(document.createdAt.desc())
                .limit(5)
                .fetch();
    }

    //cursor paging 할 경우, 이전페이지로 이동
    private BooleanExpression cursorIdControl(Long cursorId, String direction) {

        // CusorId 가 없는 경우, 가장 최신일 경우.
        if (cursorId == null)
            return null;

        // CusorId 기준 이전 정보들을 가져올 경우.
        if (direction.equals(PREVIOUS))
            return document.id.lt(cursorId);

        //CursorId 기준 최신 정보들을 가져올 경우.
        return document.id.gt(cursorId);


    }


}