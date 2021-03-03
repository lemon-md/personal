package com.camera.face.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.camera.face.service.SubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cy
 * @date 2020/12/17 10:58
 */
@RestController
@RequestMapping()
@Slf4j
@RequiredArgsConstructor
public class CheckSubscribeController {
    private final SubscriberService subscriberService;

    @GetMapping("/check")
    public String check(){
        StringBuilder sb = new StringBuilder("当前总订阅数：");
        JSONObject json = JSONObject.parseObject(subscriberService.check());
        Integer subNum = (Integer)json.get("num");
        JSONArray jsonArray = (JSONArray) json.get("subscriber");
        sb.append(subNum).append("\n");
        if (subNum > 0) {
            for (int i = 0; i < subNum; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                sb.append("订阅url=").append(jsonObject.get("address"))
                        .append(":")
                        .append(jsonObject.get("port"))
                        .append("\n")
                        .append("剩余时间:")
                        .append(jsonObject.get("timeOut"))
                        .append("秒")
                        .append("\n\n");
            }
        }
        return sb.toString();
    }

    @GetMapping("/subscribe")
    public String subscribe(){
        String result = subscriberService.subscribe();
        if (result.contains("OK")) {
            return "订阅成功";
        }
        return "订阅失败";
    }
}
