package com.carroll.biz.monitor.analyzer.auth;

/**
 * @author zhangjie
 * @create 2017-02-24 15:21
 **/
public class UserTokenEntity {

    //用户ID
    private Long userId;

    //电话号码
    private String phone;

    //token创建时间
    private long tokenCreatedTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getTokenCreatedTime() {
        return tokenCreatedTime;
    }

    public void setTokenCreatedTime(long tokenCreatedTime) {
        this.tokenCreatedTime = tokenCreatedTime;
    }
}
