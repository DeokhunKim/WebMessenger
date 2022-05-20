package com.example.WebMessenger.chat.storage.manager;


import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.ChatLogDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;
import com.example.WebMessenger.chat.domain.member.MemberGrade;

import java.util.List;


public interface ChatRepositoryManager {

    public static final int DEFAULT_GETCHATNUM = 20;

    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId, int numChat);

    public ChatLogDto getRecentRoomChatLogByRoomId(String roomId);

    public void saveChat(ChatDto chat, String roomId);

    public String createNewRoom(String roomName, List<String> members);

    public String createNewRoomDirect(String roomName, List<String> members, String roomId);

    public List<RoomInfoDto> getRoomListByUserName(String userName);

    public RoomInfoDto getRoomInfoByRoomId(String roomId);

    public boolean saveMember(String name, MemberGrade grade);

    public boolean existsByUserName(String name);


}
