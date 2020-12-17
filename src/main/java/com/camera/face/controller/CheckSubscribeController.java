package com.camera.face.controller;

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
        return subscriberService.check();
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
