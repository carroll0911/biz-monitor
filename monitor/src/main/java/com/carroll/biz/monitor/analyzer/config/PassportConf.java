package com.carroll.biz.monitor.analyzer.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限校验配置
 *
 * @author: hehongbo
 * @date 2019/9/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
@Component
@ConfigurationProperties(prefix="passport")
public class PassportConf {
    private List<String> excludes = new ArrayList<>();

    private String tokenCacheName;

    private String authToken;

    private long tokenExpireTime=3600L;

    public List<String> getExcludes() {
        return excludes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public String getTokenCacheName() {
        return tokenCacheName;
    }

    public void setTokenCacheName(String tokenCacheName) {
        this.tokenCacheName = tokenCacheName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public long getTokenExpireTime() {
        return tokenExpireTime;
    }

    public void setTokenExpireTime(long tokenExpireTime) {
        this.tokenExpireTime = tokenExpireTime;
    }
}
