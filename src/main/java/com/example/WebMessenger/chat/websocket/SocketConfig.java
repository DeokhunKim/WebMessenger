package com.example.WebMessenger.chat.websocket;

import com.example.WebMessenger.chat.domain.member.MemberGrade;
import com.example.WebMessenger.chat.domain.session.SessionMemberForm;
import com.example.WebMessenger.chat.session.ChatSessionConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.adapter.standard.StandardWebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.PostConstruct;


@Configuration
@EnableWebSocket
public class SocketConfig implements WebSocketConfigurer {

    @Autowired
    private ChatWebSocketHandler chatWebSocketHandler;

    /**
     * 핸들러를 등록하는 config
     * 추가로 http session 정보를 가지고 오기 위한 interceptor도 등록한다
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatWebSocketHandler, "/Chat")
                .addInterceptors(new SocketHandshakeInterceptor())
                .setAllowedOriginPatterns("**")
                .withSockJS();
    }

    @PostConstruct
    public void ChatBotInit() {
        WebSocketSession b1 = new StandardWebSocketSession(null, null, null, null, null);
        WebSocketSession b2 = new StandardWebSocketSession(null, null, null, null, null);
        WebSocketSession b3 = new StandardWebSocketSession(null, null, null, null, null);
        b1.getAttributes().put(ChatSessionConst.HTTP_SESSION, new SessionMemberForm("ChatBot_1", MemberGrade.CHATBOT));
        b2.getAttributes().put(ChatSessionConst.HTTP_SESSION, new SessionMemberForm("ChatBot_2", MemberGrade.CHATBOT));
        b3.getAttributes().put(ChatSessionConst.HTTP_SESSION, new SessionMemberForm("ChatBot_3", MemberGrade.CHATBOT));

        chatWebSocketHandler.sessions.add(b1);
        chatWebSocketHandler.sessions.add(b2);
        chatWebSocketHandler.sessions.add(b3);

        int size = chatWebSocketHandler.sessions.size();
    }




}
