package com.camera.face.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.camera.face.config.WebSocketServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

/**
 * tcp服务端，接受摄像机的人脸图片，
 * 1、摄像机设置一个端口
 * 2、监听端口
 * 3、摄像机开启人脸参数
 * 4、订阅摄像机Human数据（需要摄像机ip， 服务端ip
 * @author cy
 * @date 2020/10/15 11:46
 */
@Order(value = 2)
@Component
public class CameraFaceReceive implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) {
        try {
            //创建一个服务器对象，端口是摄像机端口8097
            ServerSocket serverSocket = new ServerSocket(8097);
            //创建一个客户端对象，这里的作用是用作多线程，必经服务器服务的不是一个客户端
            Socket client = null;
            boolean flag = true;
            while (flag) {
                System.out.println("服务器已启动，等待客户端请求。。。。");
                client = serverSocket.accept();
                new EchoThread(client).start();
            }
            client.close();
            serverSocket.close();
            System.out.println("服务器已关闭。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class EchoThread extends Thread {
        private Socket client;
        public EchoThread(Socket client) {
            this.client = client;
        }
        @Override
        public void run() {
            try {
                BufferedReader in = null;
                String temp = null;
                String info = "";
                boolean flag = false;
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                InetAddress inetAddress = client.getInetAddress();
                while ((temp = in.readLine()) != null) {
                    if (temp.contains("{")) {
                        flag = true;
                    }
                    if (flag) {
                        info += temp;
                    }
                }
                // 人脸识别记录
                if (info.contains("metadataObject")) {
                    Map<String, Map<String, Object>> bodyMap =  JSON.parseObject(info,new TypeReference<Map<String,Map<String, Object>>>(){});
                    JSONArray objects = (JSONArray)bodyMap.get("metadataObject").get("subImageList");
                    List<String> strings = objects.toJavaList(String.class);
                    strings.forEach(s -> {
                        JSONObject jsonObject = JSON.parseObject(s);
                        WebSocketServer.broadCastInfo("showFaceImg="+jsonObject.get("data"));
                        System.out.println("confirm");
                    });
                }
                in.close();
                client.close();
                System.out.println("关闭资源");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
