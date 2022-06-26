package com.guxian.facecheck.config;


import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class AliServiceBuilder {

    private final OssProperties props;

    public AliServiceBuilder(OssProperties ossProperties) {
        this.props = ossProperties;
    }

    @Bean
    public AliService aliServiceBean() {
        return new AliService()
                .setOssClient(
                        new OSSClientBuilder().build(props.getEndpoint(), props.getAccessKeyId()
                                , props.getAccessKeySecret()))

                .setAccessKeyId(props.getAccessKeyId())
                .setEndpoint(props.getEndpoint())
                .setBucketName(props.getBucketName())
                .setDownloadPathPrefix(props.getDownloadPrefix())
                .setAccessKeySecret(props.getAccessKeySecret())
                .setObjectNamePrefix(props.getObjectNamePrefix());
    }
}
