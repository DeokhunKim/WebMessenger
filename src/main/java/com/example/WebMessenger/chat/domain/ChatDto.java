package com.example.WebMessenger.chat.domain;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatDto {
    private String writer;
    private String message;
    private String time;

    public ChatDto(){

    }

    public ChatDto(String writer, String message, String time) {
        this.writer = writer;
        this.message = message;
        this.time = time;
    }

}
