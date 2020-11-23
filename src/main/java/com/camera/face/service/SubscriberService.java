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

    public void subscriber(){
        // TODO: 2020/11/23 摄像机http端口
        Integer port = 8097;
        // TODO: 2020/11/23 本地服务的ip
        String address = "192.168.1.159";
        // TODO: 2020/11/23 摄像机的http地址 拼接请求url
        String url = "http://192.168.1.197:8097";
        String username = "ApiAdmin";
        String password = "xinlu123321";

        Map<String, Object> map = new HashMap<>();
        map.put("Topic", "all");
        map.put("ID", 1);
        map.put("address", address);
        map.put("port", port);
        // 订阅三个月
        map.put("timeOut", 30*24*3600*3);
        String json = JSONUtil.parseFromMap(map).toString();
        // 订阅
        String result = HttpRequestUtils.sendPost(url +"/SDCAPI/V1.0/Metadata/Subscription/Subscriber", "Topic=all&ID=1", username, password, json, "json");
        log.info("订阅全部智能数据：{}",result);
        String s = HttpRequestUtils.sendGet(url+"/SDCAPI/V1.0/Metadata/Subscription", "", "ApiAdmin", "xinlu123321", "json");
        log.info("查询订阅：{}",s);
    }
}
