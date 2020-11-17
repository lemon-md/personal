package com.camera.face.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * webSocket服务
 * @author taoyou
 */
@Slf4j
@Component
@ServerEndpoint(value = "/faceWebSocketServer")
public class WebSocketServer {

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
     */
    private static final CopyOnWriteArraySet<Session> SESSION_SET = new CopyOnWriteArraySet<Session>();

    /**
     * 连接建立成功调用的方法*/
    @OnOpen
    public void onOpen(Session session) {
        SESSION_SET.add(session);
        // 在线数加1
        int cnt = ONLINE_COUNT.incrementAndGet();
        log.info("有连接加入，当前连接数为：{}", cnt);
        sendMessage(session, "webSocket连接成功~~~");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SESSION_SET.remove(session);
        int cnt = ONLINE_COUNT.decrementAndGet();
        log.info("有连接关闭，当前连接数为：{}", cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session ,String message) {
        log.info("来自客户端的消息：{}",message);
        if ("ping".equals(message)){
            sendMessage(session,"pong");
        }
    }

    /**
     * 实现服务器主动推送
     */
    public static void sendMessage(Session session ,String message){
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param message
     */
    public static void broadCastInfo(String message) {
        for (Session session : SESSION_SET) {
            if(session.isOpen()){
                sendMessage(session, message);
            }
        }
    }

    /**
     * 指定Session发送消息
     * @param sessionId
     * @param message
     */
    public static void sendMessage(String message,String sessionId) {
        Session session = null;
        for (Session s : SESSION_SET) {
            if(s.getId().equals(sessionId)){
                session = s;
                break;
            }
        }
        if(session!=null){
            sendMessage(session, message);
        }
        else{
            log.warn("没有找到你指定ID的会话：{}",sessionId);
        }
    }
}
