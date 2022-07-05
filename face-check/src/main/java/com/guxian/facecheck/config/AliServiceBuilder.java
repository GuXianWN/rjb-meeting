package com.guxian.facecheck.config;


import com.aliyun.facebody20191230.Client;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Data
@Configuration
@EnableConfigurationProperties(OssProperties.class)
public class AliServiceBuilder {

    private final OssProperties props;

    public AliServiceBuilder(OssProperties ossProperties) {
        this.props = ossProperties;
    }

    @SneakyThrows
    @Bean
    public AliServiceObject aliServiceBean() {
        return new AliServiceObject()
                .setOss(new OSSClientBuilder().build(props.getEndpoint(), props.getAccessKeyId()
                        , props.getAccessKeySecret()))
                .setClient(new Client(new Config()
                        .setAccessKeyId(props.getAccessKeyId())
                        .setEndpoint(props.getEndpoint())
                        .setAccessKeySecret(props.getAccessKeySecret())))
                .setAccessKeyId(props.getAccessKeyId())
                .setEndpoint(props.getEndpoint())
                .setBucketName(props.getBucketName())
                .setDownloadPathPrefix(props.getDownloadPrefix())
                .setAccessKeySecret(props.getAccessKeySecret())
                .setObjectNamePrefix(props.getObjectNamePrefix());
    }
}
