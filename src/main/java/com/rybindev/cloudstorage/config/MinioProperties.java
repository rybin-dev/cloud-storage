package com.rybindev.cloudstorage.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "minio")
@Component
@Data
public class MinioProperties {

    private String url;
    private String bucketName;
    private String accessKey;
    private String secretKey;
}
