package com.example.WebMessenger.chat.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RoomInfoDto {

    private String id;
    private String name;
    //TODO Change list -> set
    private List<String> members = new ArrayList<>();

    public RoomInfoDto(String id, String name, List<String> members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }
}
