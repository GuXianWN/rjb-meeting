package com.guxian.facecheck.config;

import com.aliyun.facebody20191230.Client;
import com.aliyun.oss.OSS;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

@Data
@Accessors(chain = true)
public class AliServiceObject {

    private OSS oss;

    private Client client;

    private String downloadPathPrefix;

    private String endpoint;

    private String accessKeyId;

    private String accessKeySecret;

    private String bucketName;

    private String objectNamePrefix;

}
