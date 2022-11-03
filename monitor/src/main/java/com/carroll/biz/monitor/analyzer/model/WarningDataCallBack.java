package com.carroll.biz.monitor.analyzer.model;

import lombok.Getter;
import lombok.Setter;

/**
 * 告警数据
 * Created on 202017/11/22 15:26 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Getter
@Setter
public class WarningDataCallBack extends BaseModel {
    //应用tag
    private String projectTag;
    //回调地址
    private String callback;
    //启用状态
    private boolean enable;
}
