package com.example.WebMessenger.chat.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class ChatLogDto {
    private List<ChatDto> chatLogList = new ArrayList<>();

    public ChatLogDto() {
    }
    public ChatLogDto(List<ChatDto> chatLogList) {
        this.chatLogList = chatLogList;
    }


    public void addChat(ChatDto chat) {
        chatLogList.add(0, chat);
    }



}
