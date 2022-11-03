package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.response.TraceDetailResponse;
import com.carroll.biz.monitor.analyzer.model.WarningData;
import com.carroll.biz.monitor.common.request.CurrentMonitorRequest;
import com.carroll.biz.monitor.common.request.HistoryMonitorPageRequest;
import com.carroll.biz.monitor.common.request.IdRequest;
import com.carroll.biz.monitor.common.request.MonitorStatisticRequest;
import com.carroll.biz.monitor.common.response.CurrentMonitorResponse;
import com.carroll.biz.monitor.common.response.HistoryMonitorPageResponse;
import com.carroll.biz.monitor.common.response.MonitorDetailResponse;
import com.carroll.biz.monitor.common.response.MonitorStatisticResponse;
import com.carroll.spring.rest.starter.BaseException;

/**
 * 告警数据接口
 * Created on 202017/11/22 15:35 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface IWarningDataService {

    /**
     * 获取已存在的未清除的告警数据
     *
     * @param itemId
     * @return
     */
    WarningData getCurrentData(String itemId, String applicationName, String host, String target);

    /**
     * 保存告警数据
     *
     * @param warningData
     * @return
     */
    WarningData save(WarningData warningData);


    /**
     * 定时清除僵尸数据
     */
    void clearUselessData();

    /**
     * 定时重发告警数据
     */
    void resendData();

    /**
     * 查询历史告警
     *
     * @param request
     * @return
     */
    HistoryMonitorPageResponse historyMonitor(HistoryMonitorPageRequest request);

    /**
     * 查询实时告警
     *
     * @param request
     * @return
     */
    CurrentMonitorResponse currentMonitor(CurrentMonitorRequest request);

    /**
     * 查询告警详情
     *
     * @param request
     * @return
     */
    MonitorDetailResponse monitorDetail(IdRequest request) throws BaseException;

    /**
     * 统计查询
     *
     * @param request
     * @return
     */
    MonitorStatisticResponse statistic(MonitorStatisticRequest request) throws BaseException;

    /**
     * 查询 调用链信息
     * @param traceId
     * @return
     */
    TraceDetailResponse getTraceDetail(String traceId);
}
