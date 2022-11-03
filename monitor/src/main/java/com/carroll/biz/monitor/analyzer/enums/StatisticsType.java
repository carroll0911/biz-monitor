package com.carroll.biz.monitor.analyzer.enums;

/**
 * Created on 2017/12/1 10:05 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public enum StatisticsType {
    LEVEL("告警级别"),
    NAME("告警名称"),
    SOURCE("告警源");

    private String desc;

    StatisticsType(String desc) {
        this.desc = desc;
    }
}
