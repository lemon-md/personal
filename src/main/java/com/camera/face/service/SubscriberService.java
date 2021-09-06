package com.camera.face.service;

import cn.hutool.json.JSONUtil;
import com.camera.face.config.CameraConfig;
import com.camera.face.utils.HttpRequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    CameraConfig cameraConfig;

    public void subscriber(){
        Map<String, Object> map = new HashMap<>();
        map.put("Topic", "all");
        map.put("ID", 1);
        map.put("address", cameraConfig.getAddress());
        map.put("port", cameraConfig.getPort());
        // 订阅三个月
        map.put("timeOut", 30*24*3600*3);
        String json = JSONUtil.parseFromMap(map).toString();
        // todo 订阅 记得解开
        String result = HttpRequestUtils.sendPost(cameraConfig.getUrl() +"/SDCAPI/V1.0/Metadata/Subscription/Subscriber", "Topic=all&ID=1", cameraConfig.getUsername(), cameraConfig.getPassword(), json, "json");
        log.info("订阅全部智能数据：{}",result);
        String s = HttpRequestUtils.sendGet(cameraConfig.getUrl()+"/SDCAPI/V1.0/Metadata/Subscription", "", "ApiAdmin", "xinlu123321", "json");
        log.info("查询订阅：{}",s);
    }

    public String check(){
        String s = HttpRequestUtils.sendGet(cameraConfig.getUrl()+"/SDCAPI/V1.0/Metadata/Subscription", "", "ApiAdmin", "xinlu123321", "json");
        log.info("check : {}",s);
        return s;
    }

    public String subscribe() {
        Map<String, Object> map = new HashMap<>();
        map.put("Topic", "all");
        map.put("ID", 1);
        map.put("address", cameraConfig.getAddress());
        map.put("port", cameraConfig.getPort());
        map.put("timeOut", 30*24*3600*3);
        String json = JSONUtil.parseFromMap(map).toString();
        System.out.println(json);
        String result = HttpRequestUtils.sendPost(cameraConfig.getUrl() +"/SDCAPI/V1.0/Metadata/Subscription/Subscriber", "Topic=all&ID=1", cameraConfig.getUsername(), cameraConfig.getPassword(), json, "json");
        log.info("subscribe : {}",result);
        return result;
    }
}
