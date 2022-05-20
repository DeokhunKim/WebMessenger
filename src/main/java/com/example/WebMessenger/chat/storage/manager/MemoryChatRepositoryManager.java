package com.example.WebMessenger.chat.storage.manager;

import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.ChatLogDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;
import com.example.WebMessenger.chat.domain.member.MemberGrade;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//@Repository
public class MemoryChatRepositoryManager implements ChatRepositoryManager {

    private static Map<String /*roomId*/, ChatLogDto> chatLogMapRoom = new ConcurrentHashMap<>();
    private static Map<String /*roomId*/, RoomInfoDto> roomInfoRoomIdMap = new ConcurrentHashMap<>();
    private static Map<String /*member*/, List<String>> roomListMemberMap = new ConcurrentHashMap<>();

    @Override
    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId, int numChat) {
        ChatLogDto chatLog = chatLogMapRoom.get(roomId);
        if (chatLog != null) {
            int getNumChat = numChat > chatLog.getChatLogList().size() ? chatLog.getChatLogList().size() : numChat;
            return new ChatLogDto(new ArrayList<>(chatLog.getChatLogList().subList(0, getNumChat)));
        } else {
            return new ChatLogDto();
        }
    }

    @Override
    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId) {
        return getRecentRoomChatLogByRoomId(roomId, DEFAULT_GETCHATNUM);

    }

    @Override
    public void saveChat(ChatDto chat, String roomId) {
        ChatLogDto chatLog = chatLogMapRoom.get(roomId);
        if(chatLog != null){
            chatLog.addChat(chat);
        }
        else{
            ChatLogDto newChatLog = new ChatLogDto();
            newChatLog.addChat(chat);
            chatLogMapRoom.put(roomId, newChatLog);
        }
    }

    @Override
    public List<RoomInfoDto> getRoomListByUserName(String userName) {
        if (roomListMemberMap == null || userName == null) {
            return null;
        }
        List<String> roomIdList = roomListMemberMap.get(userName);
        if(roomIdList == null) {
            return null;
        }
        List<RoomInfoDto> result = new ArrayList<>();
        for (String roomId : roomIdList) {
            RoomInfoDto roomInfo = roomInfoRoomIdMap.get(roomId);
            if( roomInfo != null ){
                result.add(roomInfo);
            }
        }
        return result;
    }

    @Override
    public RoomInfoDto getRoomInfoByRoomId(String roomId) {
        return roomInfoRoomIdMap.get(roomId);
    }

    @Override
    public String createNewRoom(String roomName, List<String> members ){
        String uuid = UUID.randomUUID().toString();
        RoomInfoDto roomInfo = new RoomInfoDto(uuid, roomName, members);
        roomInfoRoomIdMap.put(uuid, roomInfo);
        for (String member : members) {
            List<String> roomList = roomListMemberMap.get(member);
            if (roomList == null) {
                List<String> newRoomList = new ArrayList<>();
                newRoomList.add(uuid);
                roomListMemberMap.put(member, newRoomList);
            } else {
                roomList.add(uuid);
            }
        }
        return uuid;
    }

    @Override
    public String createNewRoomDirect(String roomName, List<String> members, String RoomId ){
        String uuid = RoomId;
        RoomInfoDto roomInfo = new RoomInfoDto(uuid, roomName, members);
        roomInfoRoomIdMap.put(uuid, roomInfo);
        for (String member : members) {
            List<String> roomList = roomListMemberMap.get(member);
            if (roomList == null) {
                List<String> newRoomList = new ArrayList<>();
                newRoomList.add(uuid);
                roomListMemberMap.put(member, newRoomList);
            } else {
                roomList.add(uuid);
            }
        }
        return uuid;
    }

    @Override
    public boolean saveMember(String name, MemberGrade grade) {
        return false;
    }

    @Override
    public boolean existsByUserName(String name) {
        return false;
    }

    @PostConstruct
    public void postConstruct(){
        /*
        ChatLog chatLog = new ChatLog();
        chatLog.addChat(new Chat("admin", "안녕하세요", "1647260561"));
        chatLog.addChat(new Chat("ChatBot_1", "반가워요", "1647260561"));
        chatLog.addChat(new Chat("ChatBot_1", "오늘 날씨가 좋네요", "1647260621"));
        chatLog.addChat(new Chat("admin", "테스트가 잘 되고 있나요?", "1647260621"));
        chatLog.addChat(new Chat("ChatBot_1", "잘 되고 있는 것 같습니다.", "1647260681"));

        ArrayList<String> members = new ArrayList<>(Arrays.asList("admin", "ChatBot_1"));
        String roomId = createNewRoom(members.toString() + " 대화방", members);
        chatLogMapRoom.put(roomId, chatLog);

         */
    }
}
