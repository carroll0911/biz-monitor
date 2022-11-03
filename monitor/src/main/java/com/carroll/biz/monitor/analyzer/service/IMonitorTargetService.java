package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.response.MonitorTargetDetailResponse;
import com.carroll.biz.monitor.analyzer.response.MonitorTargetPageResponse;
import com.carroll.biz.monitor.analyzer.model.MonitorTarget;
import com.carroll.biz.monitor.analyzer.request.MonitorTargetPageRequest;
import com.carroll.biz.monitor.analyzer.request.MonitorTargetSaveRequest;

/**
 * 告警对象管理
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
public interface IMonitorTargetService {

    /**
     * 保存告警对象
     * @param request
     * @return
     */
    MonitorTarget save(MonitorTargetSaveRequest request) throws MonitorBaseException;

    /**
     * 分页查询告警对象
     * @param request
     * @return
     */
    MonitorTargetPageResponse page(MonitorTargetPageRequest request);

    /**
     * 详情
     * @param id
     * @return
     */
    MonitorTargetDetailResponse detail(String id) throws MonitorBaseException;

    /**
     * 删除
     * @param id
     */
    void delete(String id) throws MonitorBaseException;

    /**
     * 根据code和projectId获取name
     * @param code
     * @param projectId
     * @return
     */
    String getTargetName(String code,String projectId);
}
