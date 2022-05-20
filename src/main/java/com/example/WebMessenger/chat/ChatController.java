package com.example.WebMessenger.chat;

import com.example.WebMessenger.chat.domain.session.SessionConst;
import com.example.WebMessenger.chat.domain.session.SessionMemberForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ChatController {

    @GetMapping(name = "ChatController", value = "/chat")
    public String chatController(HttpServletRequest request, Model model){
        model.addAttribute("userName", request.getHeader("userName"));
        return "chat/chat";
    }
}
