package com.camera.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author admin
 */
@EnableScheduling
@SpringBootApplication
public class FaceReceiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaceReceiveApplication.class, args);
    }

}
