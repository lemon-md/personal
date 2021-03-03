package com.camera.face.server;

import cn.hutool.core.net.NetUtil;
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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * tcp服务端，接受摄像机的人脸图片，
 * 1、摄像机设置一个端口
 * 2、监听端口
 * 3、摄像机开启人脸参数
 * 4、订阅摄像机Human数据（需要摄像机ip， 服务端ip）
 * @author cy
 * @date 2020/10/15 11:46
 */
@Order(value = 2)
@Component
public class CameraFaceReceive implements ApplicationRunner {
    private static String lastData = "";
    private static final AtomicInteger NUM_COUNT = new AtomicInteger(0);
    private static ServerSocket serverSocket = null;
    private static int port = 8097;

    static {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            //创建一个服务器对象，端口是摄像机端口8097
            Socket client = null;
            boolean flag = true;
            String address = NetUtil.getIpByHost(NetUtil.getLocalhostStr());
            System.out.println("服务器已启动，ip={"+address+"}");
            System.out.println("请确认端口是否是:"+port);
            while (flag) {
                client = serverSocket.accept();
                BufferedReader in = null;
                String temp = null;
                String info = "";
                boolean flag1 = false;
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((temp = in.readLine()) != null) {
                    if (temp.contains("{")) {
                        flag1 = true;
                    }
                    if (flag1) {
                        info += temp;
                    }
                }
                // 人脸识别记录
                if (info.contains("metadataObject")) {
                    Map<String, Map<String, Object>> bodyMap = JSON.parseObject(info, new TypeReference<Map<String, Map<String, Object>>>() {
                    });
                    JSONArray objects = (JSONArray) bodyMap.get("metadataObject").get("subImageList");
                    List<String> strings = objects.toJavaList(String.class);
                    strings.forEach(s -> {
                        JSONObject jsonObject = JSON.parseObject(s);
                        String data = "showFaceImg=" + jsonObject.get("data");
                        if (!lastData.equals(data)) {
                            // 发送
                            String message = NUM_COUNT.incrementAndGet() + "=showFaceImg=" + jsonObject.get("data");
                            WebSocketServer.broadCastInfo(message);
                            lastData = data;
                            System.out.println("confirm");
                        } else {
                            System.out.println("去重");
                        }
                        try {
                            Thread.sleep(1000L);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
                in.close();
                client.close();
                System.out.println("关闭资源");
            }
            client.close();
            serverSocket.close();
            System.out.println("服务器已关闭。");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
