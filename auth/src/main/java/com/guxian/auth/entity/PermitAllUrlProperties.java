package com.guxian.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "auth")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermitAllUrlProperties {
    private List<String> url;
}
