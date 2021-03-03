package com.camera.face.service;

import cn.hutool.json.JSONUtil;
import com.camera.face.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cy
 * @date 2020/11/17 11:30
 */
@Slf4j
@Component
public class SubscriberService {
    //  2020/11/23 服务端的http监听端口
    Integer port = 8097; //只是一个参数，与摄像头通不通无关 与CameraFaceReceive的port参数相同
    // 自动获取本地ip
    String address = "192.168.20.11"; //部署公网方案
    //String address = NetUtil.getIpByHost(NetUtil.getLocalhostStr());
    //String address = "192.168.1.173"; // 只是一个参数，与摄像头通不通无关
    //  2020/11/23 摄像机的http地址 拼接请求url
    String url = "http://192.168.20.10:8097"; //  使用frp方案
    //String url = "http://192.168.1.197:8097"; // 与摄像头通不通有关
    String username = "ApiAdmin";// 与摄像头通不通有关
    String password = "xinlu123321";// 与摄像头通不通有关

    public void subscriber(){
        Map<String, Object> map = new HashMap<>();
        map.put("Topic", "all");
        map.put("ID", 1);
        map.put("address", address);
        map.put("port", port);
        // 订阅三个月
        map.put("timeOut", 30*24*3600*3);
        String json = JSONUtil.parseFromMap(map).toString();
        // todo 订阅 记得解开
        String result = HttpRequestUtils.sendPost(url +"/SDCAPI/V1.0/Metadata/Subscription/Subscriber", "Topic=all&ID=1", username, password, json, "json");
        log.info("订阅全部智能数据：{}",result);
        String s = HttpRequestUtils.sendGet(url+"/SDCAPI/V1.0/Metadata/Subscription", "", "ApiAdmin", "xinlu123321", "json");
        log.info("查询订阅：{}",s);
    }

    public String check(){
        String s = HttpRequestUtils.sendGet(url+"/SDCAPI/V1.0/Metadata/Subscription", "", "ApiAdmin", "xinlu123321", "json");
        log.info("check : {}",s);
        return s;
    }

    public String subscribe() {
        Map<String, Object> map = new HashMap<>();
        map.put("Topic", "all");
        map.put("ID", 1);
        map.put("address", address);
        map.put("port", port);
        map.put("timeOut", 30*24*3600*3);
        String json = JSONUtil.parseFromMap(map).toString();
        System.out.println(json);
        String result = HttpRequestUtils.sendPost(url +"/SDCAPI/V1.0/Metadata/Subscription/Subscriber", "Topic=all&ID=1", username, password, json, "json");
        log.info("subscribe : {}",result);
        return result;
    }
}
