package com.hanghae.final_project.domain.repository.workspace;

import com.hanghae.final_project.api.workspace.dto.request.PagingRequestDto;
import com.hanghae.final_project.api.workspace.dto.request.SearchDocumentRequestDto;
import com.hanghae.final_project.api.workspace.dto.response.DocumentResponseDto;
import com.hanghae.final_project.domain.model.Document;
import com.hanghae.final_project.global.dto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.hanghae.final_project.domain.model.QDocument.document;


@Slf4j
@Repository
@RequiredArgsConstructor
public class DocumentQueryRepository {

    public static final String CONTENT_TITLE = "ContentTitle";
    public static final String WRITER = "Writer";

    public static final String PREVIOUS = "Previous";

    public static final String RECENT = "Recent";

    public static final Integer PAGING_SIZE = 10;
    private final JPAQueryFactory queryFactory;


    public ResponseDto<List<DocumentResponseDto>> getDocumentWithPaging(Long workspaceId, PagingRequestDto requestDto) {


        if (requestDto == null || requestDto.getDirection() == null)
            requestDto = PagingRequestDto.builder()
                    .direction(PREVIOUS)
                    .build();

        List<Document> documentList = getDocumentWithCursorPagination(workspaceId, requestDto);

        if (requestDto.getDirection().equals(RECENT) && documentList.size() != PAGING_SIZE && documentList.size() != 0) {

            PagingRequestDto addRequestDto = PagingRequestDto.builder()
                    .direction(PREVIOUS)
                    .cursorId(documentList.get(documentList.size() - 1).getId())
                    .build();

            List<Document> addList = getDocumentWithCursorPagination(workspaceId, addRequestDto, documentList.size());

            documentList = Stream.of(documentList, addList)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

        }
        return ResponseDto.success(
                documentList.stream()
                        .map(DocumentResponseDto::of)
                        .collect(Collectors.toList()));

    }


    public ResponseDto<List<DocumentResponseDto>> searchDocumentByFilter(Long workspaceId,
                                                                         SearchDocumentRequestDto requestDto) {

        // 제목 & 내용으로 검색하는 경우
        if (requestDto.getType().equals(CONTENT_TITLE)) {

            List<DocumentResponseDto> documentResponseDtos =
                    searchDocumentByKeyword(workspaceId, requestDto)
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


    private List<Document> getDocumentWithCursorPagination(Long workspaceId, PagingRequestDto requestDto) {


        if (requestDto.getDirection().equals(RECENT)){

            final Comparator<Document> comparator = Comparator.comparingLong(Document::getId).reversed();

            return queryFactory
                    .selectFrom(document)
                    .where(document.workSpace.id.eq(workspaceId),
                            cursorIdControl(requestDto.getCursorId(), requestDto.getDirection()))
                    .limit(PAGING_SIZE)
                    .orderBy(document.id.asc())
                    .fetch().stream().sorted(comparator).collect(Collectors.toList());
        }

        return queryFactory
                .selectFrom(document)
                .where(document.workSpace.id.eq(workspaceId),
                        cursorIdControl(requestDto.getCursorId(), requestDto.getDirection()))
                .limit(PAGING_SIZE)
                .orderBy(document.id.desc())
                .fetch();

    }

    private List<Document> getDocumentWithCursorPagination(Long workspaceId, PagingRequestDto requestDto, int documentSize) {

        return queryFactory.selectFrom(document)
                .where(document.workSpace.id.eq(workspaceId),
                        cursorIdControl(requestDto.getCursorId(), PREVIOUS))
                .orderBy(document.createdAt.desc())
                .limit(PAGING_SIZE - documentSize)
                .fetch();
    }

    private List<Document> searchDocumentByKeyword(Long workspaceId, SearchDocumentRequestDto requestDto) {

        log.info("내용 및 제목을 통해 문서 검색 시작");
        log.info("검색어 : {}", requestDto.getKeyword());
        return queryFactory
                .selectFrom(document)
                .where(document.title.contains(requestDto.getKeyword()).or(document.content.contains(requestDto.getKeyword())),
                        document.workSpace.id.eq(workspaceId),
                        cursorIdControl(requestDto.getCursorId(), requestDto.getDirection())
                )
                .orderBy(document.createdAt.desc())
//                .limit(5)
                .fetch();
    }

    private List<Document> searchDocumentByWriter(Long workspaceId, SearchDocumentRequestDto requestDto) {

        log.info("작성자를 통해 문서 검색 시작");
        log.info("작성자 : {}", requestDto.getWriter());
        return queryFactory
                .selectFrom(document)
                .where(document.user.nickname.contains(requestDto.getWriter()),
                        document.workSpace.id.eq(workspaceId),
                        cursorIdControl(requestDto.getCursorId(), requestDto.getDirection()))
                .orderBy(document.createdAt.desc())
//                .limit(5)
                .fetch();
    }

    //cursor paging 할 경우, 이전페이지로 이동
    private BooleanExpression cursorIdControl(Long cursorId, String direction) {

        // CusorId 가 없는 경우, 가장 최신일 경우.
        if (cursorId == null || direction == null)
            return null;

        // CusorId 기준 이전 정보들을 가져올 경우.
        if (direction.equals(PREVIOUS))
            return document.id.lt(cursorId);

        //CursorId 기준 최신 정보들을 가져올 경우.
        return document.id.gt(cursorId);


    }


}
