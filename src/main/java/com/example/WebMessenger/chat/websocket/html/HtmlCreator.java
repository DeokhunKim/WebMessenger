package com.example.WebMessenger.chat.websocket.html;

import com.example.WebMessenger.chat.domain.ChatDto;
import com.example.WebMessenger.chat.domain.RoomInfoDto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HtmlCreator {
    public static final boolean MyChat = true;
    public static final boolean OtherChat = false;

    public static String getRoomByRoomInfo(RoomInfoDto roomInfo) {
        return "<a href=\"#\" class=\"list-group-item list-group-item-action border-0\" id=\"" + roomInfo.getId() + "\">\n" +
                // TODO 새 대화 갯수 시스템
                //"\t<div class=\"badge bg-success float-right\">5</div>\n" +
                "\t<div class=\"d-flex align-items-start\">\n" +
                // TODO 프로필 사진 시스템
                "\t\t<img src=\"https://bootdey.com/img/Content/avatar/avatar1.png\" class=\"rounded-circle mr-1\" alt=\"Vanessa Tucker\" width=\"40\" height=\"40\">\n" +
                "\t\t<div class=\"flex-grow-1 ml-3\">\n" +
                "\t\t\t"+ roomInfo.getName() + "\n" +
                // TODO 온라인 여부 시스템
                "\t\t\t<div class=\"small\"><span class=\"fas fa-circle chat-online\"></span> Online</div>\n" +
                "\t\t</div>\n" +
                "\t</div>\n" +
                "</a>\n";
    }

    public static String getChat(ChatDto chat, boolean isMyChat) {
        // 내가 쓴 채팅이면
        if (isMyChat == MyChat) {
           return "<div class=\"chat-message-right pb-4\">\n" +
                    "\t<div>\n" +
                    // TODO 프로필 사진 시스템
                    "\t\t<img src=\"https://bootdey.com/img/Content/avatar/avatar2.png\" class=\"rounded-circle mr-1\" alt=\"Chris Wood\" width=\"40\" height=\"40\">\n" +
                    "\t\t<div class=\"text-muted small text-nowrap mt-2\">" + dateToTimeonly(chat.getTime()) + "</div>\n" +
                    "\t</div>\n" +
                    "\t<div class=\"flex-shrink-1 bg-light rounded py-2 px-3 mr-3\">\n" +
                    "\t\t<div class=\"font-weight-bold mb-1\">You</div>\n" +
                    "\t\t"+ chat.getMessage() + "\n" +
                    "\t</div>\n" +
                    "</div>\n";

        }
        // 상대가 쓴 채팅이면
        else {
            return "<div class=\"chat-message-left pb-4\">\n" +
                    "\t<div>\n" +
                    // TODO 프로필 사진 시스템
                    "\t\t<img src=\"https://bootdey.com/img/Content/avatar/avatar1.png\" class=\"rounded-circle mr-1\" alt=\"Sharon Lessman\" width=\"40\" height=\"40\">\n" +
                    "\t\t<div class=\"text-muted small text-nowrap mt-2\">"+ dateToTimeonly(chat.getTime()) + "</div>\n" +
                    "\t</div>\n" +
                    "\t<div class=\"flex-shrink-1 bg-light rounded py-2 px-3 ml-3\">\n" +
                    "\t\t<div class=\"font-weight-bold mb-1\">" + chat.getWriter() + "</div>\n" +
                    "\t\t" + chat.getMessage() + "\n" +
                    "\t</div>\n" +
                    "</div>\n";
        }
    }

    private static String dateToTimeonly(String time) {
        return new SimpleDateFormat("hh:mm").format(new Date(Long.parseLong(time)));
    }

}
