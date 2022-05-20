package com.example.WebMessenger.chat.storage.manager;

import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.ChatLogDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;
import com.example.WebMessenger.chat.domain.member.MemberGrade;
import com.example.WebMessenger.chat.storage.entity.mongo.Chat;
import com.example.WebMessenger.chat.storage.entity.mongo.Member;
import com.example.WebMessenger.chat.storage.entity.mongo.RoomInfo;
import com.example.WebMessenger.chat.storage.repository.mongo.ChatRepository;
import com.example.WebMessenger.chat.storage.repository.mongo.MemberRepository;
import com.example.WebMessenger.chat.storage.repository.mongo.RoomInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
//@Repository
public class MongoChatRepositoryManager implements ChatRepositoryManager {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId, int numChat) {

        List<Chat> chatList = chatRepository.findByRoomId(roomId)
                .stream()
                .limit(numChat)
                .collect(Collectors.toList());

        ChatLogDto chatLogDto = new ChatLogDto();
        for (Chat chat : chatList) {
            chatLogDto.addChat(new ChatDto(chat.getWriter(), chat.getMessage(), chat.getTime()));
        }

        return chatLogDto;
    }


    @Override
    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId) {
        return getRecentRoomChatLogByRoomId(roomId, DEFAULT_GETCHATNUM);
    }

    //@Override
    /*
    public void saveChat_v1(ChatDto chat, String roomId) {
        RoomInfo roomInfo = roomInfoRepository.findByRoomId(roomId);
        roomInfo.getChatList().add(new Chat(chat.getWriter(), chat.getTime(), chat.getMessage()) );
        roomInfoRepository.save(roomInfo);
    }*/

    //@Override
    /*
    public void saveChat_v2(ChatDto chat, String roomId) {
        String roomObjectId = roomInfoRepository.findByRoomId(roomId).getId();
        Query query = new Query(Criteria.where("_id").is(roomObjectId));
        Update update = new Update();
        update.push("chatList", new Chat(chat.getWriter(), chat.getTime(), chat.getMessage()) );

        mongoTemplate.updateFirst(query, update, RoomInfo.class);
    }

     */

    @Override
    public void saveChat(ChatDto chat, String roomId) {
        chatRepository.save(new Chat(roomId, chat.getWriter(), chat.getTime(), chat.getMessage()));
    }

    @Override
    public String createNewRoom(String roomName, List<String> members) {
        String roomId = UUID.randomUUID().toString();
        return createNewRoomDirect(roomName, members, roomId);
    }

    @Override
    public String createNewRoomDirect(String roomName, List<String> members, String roomId) {
        for (String member : members) {
            Member findMember = memberRepository.findByUserName(member);
            findMember.getRoomList().add(roomId);
            memberRepository.save(findMember);
        }

        RoomInfo roominfo = new RoomInfo(roomId, roomName);
        roominfo.setMembers(members.stream().collect(Collectors.toSet()));
        roomInfoRepository.save(roominfo);

        return roomId;
    }

    @Override
    public List<RoomInfoDto> getRoomListByUserName(String userName) {
        List<RoomInfoDto> roomInfoDtoList = new ArrayList<>();
        for (String roomId : memberRepository.findByUserName(userName).getRoomList()) {
            roomInfoDtoList.add(getRoomInfoByRoomId(roomId));
        }
        return roomInfoDtoList;
    }

    @Override
    public RoomInfoDto getRoomInfoByRoomId(String roomId) {
        RoomInfo roomInfo = roomInfoRepository.findByRoomId(roomId);
        return transRoomInfoToDto(roomInfo);
    }

    @Override
    public boolean saveMember(String name, MemberGrade grade) {
        if (false == memberRepository.existsByUserName(name)) {
            memberRepository.save(new Member(name, grade));
            return true;
        }
        return false;
    }

    public boolean existsByUserName(String name) {
        return memberRepository.existsByUserName(name);
    }

    RoomInfoDto transRoomInfoToDto(RoomInfo roomInfo){
        return new RoomInfoDto(roomInfo.getRoomId(), roomInfo.getRoomName(),
                roomInfo.getMembers().stream().collect(Collectors.toList()));
    }

    @PostConstruct
    private void init(){
        memberRepository.deleteAll();
        roomInfoRepository.deleteAll();
        chatRepository.deleteAll();
    }
}
