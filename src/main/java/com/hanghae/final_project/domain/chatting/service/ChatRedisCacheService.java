package com.hanghae.final_project.domain.chatting.service;

import com.hanghae.final_project.domain.chatting.dto.ChatRoomDto;
import com.hanghae.final_project.domain.chatting.dto.request.ChatMessageSaveDto;
import com.hanghae.final_project.domain.chatting.dto.request.ChatPagingDto;
import com.hanghae.final_project.domain.chatting.dto.response.ResChatPagingDto;
import com.hanghae.final_project.domain.chatting.model.Chat;
import com.hanghae.final_project.domain.chatting.repository.ChatRepository;
import com.hanghae.final_project.domain.chatting.utils.ChatUtils;
import com.hanghae.final_project.domain.user.model.User;
import com.hanghae.final_project.domain.user.repository.UserRepository;
import com.hanghae.final_project.domain.workspace.model.WorkSpace;
import com.hanghae.final_project.domain.workspace.repository.WorkSpaceRepository;
import com.hanghae.final_project.global.commonDto.ResponseDto;
import com.hanghae.final_project.global.exception.ErrorCode;
import com.hanghae.final_project.global.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository.CHAT_ROOMS;
import static com.hanghae.final_project.domain.chatting.repository.ChatRoomRepository.CHAT_SORTED_SET_;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRedisCacheService {

    private final ChatUtils chatUtils;

    public static final String NEW_CHAT = "NEW_CHAT";

    public static final String USERNAME_NICKNAME = "USERNAME_NICKNAME";
    private final RedisTemplate<String, Object> redisTemplate;

    private final ChatRepository chatRepository;

    private final WorkSpaceRepository workSpaceRepository;

    private final UserRepository userRepository;

    private final RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate;

    private final RedisTemplate<String,String> roomRedisTemplate;

    private ZSetOperations<String, ChatMessageSaveDto> zSetOperations;
    private HashOperations<String, String, ChatRoomDto> opsHashChatRoom;


    // (마지막 refactoring에는 삭제)
    // Test를 위한 적재해놓는 작업
    //DB에 존재하는 workspace CHAT_ROOM_ID를 redis에 미리 서버 띄울 때, 적재해놓기.
    @PostConstruct
    private void init() {

        opsHashChatRoom = redisTemplate.opsForHash();
        zSetOperations = chatRedisTemplate.opsForZSet();
        //DB 로부터 workspace_id 가져와서
        List<WorkSpace> workSpaceList = workSpaceRepository.findAll();
        for (WorkSpace workSpace : workSpaceList) {
            opsHashChatRoom.put(CHAT_ROOMS,
                    workSpace.getId().toString(),
                    ChatRoomDto.create(workSpace.getId().toString())
            );
        }


    }

    //채팅 메시지가 왔을 때, redis에 적재해놓기
    public void addChat(ChatMessageSaveDto chatMessageSaveDto) {

        //닉네임 변경이 가능하기 때문에, 저장 따로진행 x
       ChatMessageSaveDto savedData = ChatMessageSaveDto.createChatMessageSaveDto(chatMessageSaveDto);
        // writeBack 용 새로운 채팅 데이터 저장
        redisTemplate.opsForZSet().add(NEW_CHAT, savedData, chatUtils.changeLocalDateTimeToDouble(savedData.getCreatedAt()));
        // caching 용 데이터 저장
        redisTemplate.opsForZSet().add(CHAT_SORTED_SET_ + savedData.getRoomId(), savedData, chatUtils.changeLocalDateTimeToDouble(savedData.getCreatedAt()));

    }

    //WorkspaceId 를 통해서 채팅 내용 조회 Paging 처리
    @Transactional(readOnly = true)
    //@Cacheable(key = "#workSpaceId + #cursor",value = "chatData",cacheManager = "redisCacheManager")
    public ResponseDto<List<ResChatPagingDto>> getChats(Long workSpaceId, String cursor) {


        Slice<Chat> chatSlice =
                chatRepository
                        .findAllByCreatedAtBeforeAndWorkSpace_IdOrderByCreatedAtDesc(
                                cursor,
                                workSpaceId,
                                PageRequest.of(0, 10)
                        );

        List<ResChatPagingDto> chats = chatSlice.stream()
                .map(ResChatPagingDto::of)
                .collect(Collectors.toList());

        return ResponseDto.success(chats);

    }

    //우선 기본적으로 redis로 부터 데이터가 있는지 확인하자.
    public ResponseDto<List<ResChatPagingDto> > getChatsFromRedis(Long workSpaceId, ChatPagingDto chatPagingDto) {

        //마지막 채팅을 기준으로 redis의 Sorted set에 몇번째 항목인지 파악
        ChatMessageSaveDto cursorDto = ChatMessageSaveDto.builder()
                .type(ChatMessageSaveDto.MessageType.TALK)
                .roomId(workSpaceId.toString())
                .createdAt(chatPagingDto.getCursor())
                .message(chatPagingDto.getMessage())
                .writer(chatPagingDto.getWriter())
                .build();


        //Range 범위 구하기 위해서, 마지막 채팅 데이터를 넣고 최신에서 몇번째인지 값 구하기
        Long rank = zSetOperations.reverseRank(CHAT_SORTED_SET_ + workSpaceId, cursorDto);

        log.info("cursor rank {}",rank);
        //만약 맨처음 paging일 경우 마지막 채팅이 없기 때문에, 0~10번까지의 채팅을 내보낸다.
        if (rank == null)
            rank = 0L;
        else rank = rank + 1;

        //sorted set으로부터 채팅 순서대로 불러오기
        Set<ChatMessageSaveDto> chatMessageSaveDtoSet = zSetOperations.reverseRange(CHAT_SORTED_SET_ + workSpaceId, rank, rank + 10);

        //Stream을 사용해서 List로 변환 후 ,
        //이 변환이 필요할까에 대해서는 나중에 좀 더 생각해보자.
        List<ResChatPagingDto> chatMessageDtoList =
                chatMessageSaveDtoSet
                        .stream()
                        .map(ResChatPagingDto::byChatMessageDto)
                        .collect(Collectors.toList());

        //만약 채팅 데이터가 10개가 아니라면, DB에 데이터가 더 있는지 확인해야함
        if(chatMessageDtoList.size()!=10 ){
            log.info("데이터가 10개가 아닙니다, DB 검색을 시작합니다.");
            findOtherChatDataInMysql(chatMessageDtoList,workSpaceId ,chatPagingDto.getCursor());
        }

        for(ResChatPagingDto resChatPagingDto : chatMessageDtoList){
            resChatPagingDto.setNickname( findUserNicknameByUsername(resChatPagingDto.getWriter()) );

        }

        return ResponseDto.success(chatMessageDtoList);
    }

    public void cachingDBDataToRedis(Chat chat){
        ChatMessageSaveDto chatMessageSaveDto = ChatMessageSaveDto.of(chat);
        redisTemplate.opsForZSet().add(CHAT_SORTED_SET_ + chatMessageSaveDto.getRoomId(), chatMessageSaveDto, chatUtils.changeLocalDateTimeToDouble(chatMessageSaveDto.getCreatedAt()));

    }

    public String findUserNicknameByUsername(String username){

        //redis 에 닉네임이 존재하는지 확인,
        String nickname = (String)roomRedisTemplate.opsForHash().get(USERNAME_NICKNAME,username);

        if(nickname!=null)
            return nickname;

        //redis 에  닉네임이 존재하지 않는다면, MYSQL에서 데이터 불러오기
        User user =userRepository.findByUsername(username)
                .orElseThrow(()->new RequestException(ErrorCode.USER_NOT_EXIST));
        // caching 하기
        roomRedisTemplate.opsForHash().put(USERNAME_NICKNAME,username,user.getNickname());

        return user.getNickname();
    }

    public void changeUserCachingNickname(String username,String changedNickname){
        roomRedisTemplate.opsForHash().put(USERNAME_NICKNAME,username,changedNickname);
    }

    private void findOtherChatDataInMysql(List<ResChatPagingDto> chatMessageDtoList, Long workSpaceId, String cursor ){

        String lastCursor;
        // 데이터가 하나도 없을 경우 현재시간을 Cursor로 활용
        if(chatMessageDtoList.size()==0 && cursor==null){
            log.info("redis cache에 해당하는 데이터가 하나도 없습니다. DB 확인을 진행합니다.");
            lastCursor = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        }
        //redis 적재된 마지막 데이터를 입력했을 경우.
        else if(chatMessageDtoList.size()==0 && cursor!=null){
            lastCursor=cursor;
        }
        // 데이터가 존재할 경우 CreatedAt을 Cursor로 사용
        else lastCursor = chatMessageDtoList.get(chatMessageDtoList.size()-1).getCreatedAt();



        int dtoListSize = chatMessageDtoList.size();
        Slice<Chat> chatSlice =
                chatRepository
                        .findAllByCreatedAtBeforeAndWorkSpace_IdOrderByCreatedAtDesc(
                                lastCursor,
                                workSpaceId,
                                PageRequest.of(0, 30)
                        );

        //redis에 적재하는 건 Thread 로 돌리는걸로 나중에 바꾸자.
        for(Chat chat:chatSlice.getContent()){
            cachingDBDataToRedis(chat);
        }


        //응답에 부족한 데이터 추가
        //추가 데이터가 없을 때 return;
        if(chatSlice.getContent().isEmpty())
            return;

        //추가 데이터가 존재하다면, responseDto에  데이터 추가.
        for(int i = dtoListSize ; i <=10;i++){
            try{
                Chat chat = chatSlice.getContent().get( i - dtoListSize );
                chatMessageDtoList.add(ResChatPagingDto.of(chat));
            }catch (IndexOutOfBoundsException e){
                return;
            }
        }
    }
}
