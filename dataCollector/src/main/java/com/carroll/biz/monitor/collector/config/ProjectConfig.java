package com.carroll.biz.monitor.collector.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 项目配置
 * Created on 202017/11/21 17:09 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Data
@Component
@ConfigurationProperties(prefix="monitor.project")
public class ProjectConfig {

    private String tag;
    private String password;
    private String applicationName;
}
