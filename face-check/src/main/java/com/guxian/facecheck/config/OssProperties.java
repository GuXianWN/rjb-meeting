package com.guxian.facecheck.config;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "oss",value = "oss")
public class OssProperties {
    private String downloadPrefix;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String objectNamePrefix;

}
