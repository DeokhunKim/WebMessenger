package com.example.WebMessenger.chat.websocket;

import com.example.WebMessenger.chat.domain.member.MemberGrade;
import com.example.WebMessenger.chat.domain.session.SessionConst;
import com.example.WebMessenger.chat.domain.session.SessionMemberForm;
import com.example.WebMessenger.chat.session.ChatSessionConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

//https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#websocket-server-handshake
public class SocketHandshakeInterceptor implements HandshakeInterceptor {

    /**
     * Socket Handshake 전에 해당 Session의 user 정보을 attribute 로 저장
     * Socket Handler에서는 httpSession에 접근 할 방법이 없기 때문임
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;


            Cookie token = Arrays.stream(servletRequest.getServletRequest().getCookies())
                    .filter(cookie -> cookie.getName().equals("kpp_t"))
                    .findFirst().orElse(null);
            Claims body = Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString("TheKoon".getBytes()))
                    .parseClaimsJws(token.getValue()).getBody();
            ArrayList<String> roles = (ArrayList<String>) body.get("roles");

            SessionMemberForm member = new SessionMemberForm(body.getSubject(), MemberGrade.valueOf(roles.get(0)));
            attributes.put(ChatSessionConst.HTTP_SESSION, member);
        }
       return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
