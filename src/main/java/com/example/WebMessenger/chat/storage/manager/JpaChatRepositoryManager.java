package com.example.WebMessenger.chat.storage.manager;

import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.ChatLogDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;
import com.example.WebMessenger.chat.domain.member.MemberGrade;
import com.example.WebMessenger.chat.storage.entity.normal.Chat;
import com.example.WebMessenger.chat.storage.entity.normal.Member;
import com.example.WebMessenger.chat.storage.entity.normal.MemberRoomInfo;
import com.example.WebMessenger.chat.storage.entity.normal.RoomInfo;
import com.example.WebMessenger.chat.storage.repository.jpa.JpaChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Repository
@Transactional
public class JpaChatRepositoryManager implements ChatRepositoryManager{

    @Autowired
    JpaChatRepository jpaChatRepository;

    @PersistenceContext
    EntityManager em;

    @Override
    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId, int numChat) {
        ChatLogDto chatLog = new ChatLogDto();
        List<Chat> findChatLogList = jpaChatRepository.GetRoomChatPageByRoomId(roomId, PageRequest.of(0, numChat)).toList();
        if (findChatLogList.isEmpty()) {
            return chatLog;
        }
        findChatLogList.stream()
                .forEach(entity -> chatLog.addChat(new ChatDto(entity.getWriter(), entity.getMessage(), entity.getTime())));
        return chatLog;
    }

    @Override
    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId) {
        return getRecentRoomChatLogByRoomId(roomId, DEFAULT_GETCHATNUM);
    }

    @Override
    public void saveChat(ChatDto chat, String roomId) {
        if(jpaChatRepository.existsById(roomId)){
            RoomInfo roomInfo = jpaChatRepository.getById(roomId);
            //roomInfo.getChatList().add(0, new Chat(roomInfo, chat.getWriter(), chat.getTime(), chat.getMessage()));

            Chat chatIns = new Chat(roomInfo, chat.getWriter(), chat.getTime(), chat.getMessage());
            em.persist(chatIns);

        }
        else{
            log.error("Try saveChat to not exist room.");
        }
    }

    @Override
    public String createNewRoom(String roomName, List<String> members) {
        String roomId = UUID.randomUUID().toString();
        return createNewRoomDirect(roomName, members, roomId);
    }

    @Override
    public String createNewRoomDirect(String roomName, List<String> members, String roomId) {
        RoomInfo roomInfo = new RoomInfo(roomId, roomName);
        em.persist(roomInfo);
        for (String memberStr : members) {
            Member member = jpaChatRepository.getMemberByUserName(memberStr);
            MemberRoomInfo memberRoomInfo = new MemberRoomInfo(member, roomInfo);
            em.persist(memberRoomInfo);
            // TODO 테스트 해볼 것. 영속성 콘텍스트.
            em.flush();
            em.clear();
        }

        return roomId;
    }

    @Override
    public List<RoomInfoDto> getRoomListByUserName(String userName) {
        List<RoomInfoDto> roomInfoDtoList = new ArrayList<>();
        List<RoomInfo> roomListByUserName = jpaChatRepository.getRoomListByUserName(userName);
        for (RoomInfo roomInfo : roomListByUserName) {
            roomInfoDtoList.add(transRoomInfoToDto(roomInfo));
        }
        return roomInfoDtoList;
    }

    @Override
    public RoomInfoDto getRoomInfoByRoomId(String roomId) {
        return transRoomInfoToDto(jpaChatRepository.getById(roomId));
    }

    @Override
    public boolean saveMember(String name, MemberGrade grade) {
        if (jpaChatRepository.getMemberByUserName(name) != null) {
            return false;
        }
        Member memberEntity = new Member(name, grade);
        em.persist(memberEntity);
        return true;
    }

    @Override
    public boolean existsByUserName(String name) {
        return false;
    }

    RoomInfoDto transRoomInfoToDto(RoomInfo roomInfo){
        List<String> members = new ArrayList<>();
        for (MemberRoomInfo memberRoomInfo : roomInfo.getMemberList()) {
            members.add(memberRoomInfo.getMember().getUserName());
        }
        return new RoomInfoDto(roomInfo.getRoomId(), roomInfo.getRoomName(), members);
    }
}
