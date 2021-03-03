package com.camera.face.server;

import com.camera.face.service.SubscriberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author cy
 * @date 2020/11/17 11:07
 */
@Slf4j
@Order(value = 1)
@Component
public class SubscriberAllRunner implements ApplicationRunner {
    @Autowired
    private SubscriberService subscriberService;
    @Override
    public void run(ApplicationArguments args) {
        subscriberService.subscribe();
        subscriberService.check();
    }
}
