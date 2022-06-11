package com.example.WebMessenger.chat;

import com.example.WebMessenger.chat.domain.session.SessionConst;
import com.example.WebMessenger.chat.domain.session.SessionMemberForm;
import com.example.WebMessenger.chat.jwt.ReadJWT;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
public class ChatController {

    @GetMapping(name = "ChatController", value = "/chat")
    public String chatController(HttpServletRequest request, Model model){
        String token = Arrays.stream(request.getCookies()).filter(c -> c.getName().equals("kpp_t"))
                .findFirst().orElse(null).getValue();
        String user = ReadJWT.getUser(token);

        model.addAttribute("userName", user);
        return "chat/chat";
    }
}
