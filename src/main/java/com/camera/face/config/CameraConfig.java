package com.camera.face.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author cy
 * @date 2021/5/10 10:45
 */
@Component
@Data
@ConfigurationProperties(prefix = "camera")
public class CameraConfig {

    private Integer port;

    private String url;

    private String username;

    private String password;

    private String address;
}
