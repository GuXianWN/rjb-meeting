package com.guxian.facecheck.config;

import com.aliyun.oss.OSS;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AliService {
    private OSS ossClient;
    private String downloadPathPrefix;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String objectNamePrefix;
}
