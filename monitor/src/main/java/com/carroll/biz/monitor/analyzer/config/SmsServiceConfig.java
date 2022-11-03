package com.carroll.biz.monitor.analyzer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 短信平台配置信息
 * Created on 202017/12/7 14:43 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Component
@ConfigurationProperties(prefix="monitor.sms")
@Data
public class SmsServiceConfig {
    private String syscode;
    private String password;
    private String code;
    private String uri;
}
