package com.carroll.biz.monitor.analyzer.enums;

/**
 * @author: hehongbo
 * @date 2019/9/9
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public enum ErrEnum {
    LOGIN_ERROR("AUTH.0001", "用户名或密码错误"),
    NOT_LOGIN_ERROR("AUTH.0002", "未登录"),
    LOG_OUT_ERROR("AUTH.0003", "注销登录失败"),
    NO_PERMISSION_ERROR("AUTH.0004", "无权执行此操作"),
    PWD_ERROR("AUTH.0005", "密码错误"),
    USER_NOT_EXISTS("USER.0001", "用户不存在"),
    USER_EXISTS("USER.0002", "用户已存在"),
    DATA_NOT_EXIST("COMMON.0000", "数据不存在"),
    DATA_EXIST("COMMON.0001", "数据存在"),
    PROJECT_TAG_ALREADY_EXIST("PROJECT.0001", "监控项目tag已存在"),
    ITEM_TAG_ALREADY_EXIST("ITEM.0001", "监控项tag已存在");

    private String errCode;
    private String errMsg;

    ErrEnum(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public String getCode() {
        return errCode;
    }

    public String getMsg() {
        return errMsg;
    }
}
