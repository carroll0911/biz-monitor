package com.carroll.biz.monitor.analyzer.enums;

/**
 * 用户角色
 *
 * @author: hehongbo
 * @date 2019/9/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public enum Role {
    SUPPER("超级管理员"),
    ADMIN("管理员"),
    NORMAL("普通用户");

    private String desc;

    Role(String desc) {
        this.desc = desc;
    }
}
