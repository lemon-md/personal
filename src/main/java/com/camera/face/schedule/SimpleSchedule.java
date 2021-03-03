package com.camera.face.schedule;

import com.camera.face.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author cy
 */
@Slf4j
@Configuration
public class SimpleSchedule {
    @Autowired
    private SubscriberService subscriberService;
    /**
     * 定时任务 一个月定时去刷新订阅事件
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    private void checkSubscriberStatus() {
        System.out.println("定时任务--刷新订阅");
        subscriberService.check();
        subscriberService.subscriber();
    }
}