package com.carroll.biz.monitor.data.common.dto;

/**
 * Created on 202017/11/22 15:05 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public class KafkaTopic {

    private KafkaTopic(){}

    /**
     * 监控数据Topic
     */
    public static final String MONITOR_DATA="MONITOR_DATA";
    /**
     * 通知发送数据Topic
     */
    public static final String NOTIFY_DATA="NOTIFY_DATA";

    /**
     * 时间监控 Group
     */
    public static final String MONITOR_TIME_GROUP="MONITOR_TIME_GROUP";

    /**
     * 监控数据分析 Group
     */
    public static final String MONITOR_DATA_GROUP="MONITOR_DATA_GROUP";

}
