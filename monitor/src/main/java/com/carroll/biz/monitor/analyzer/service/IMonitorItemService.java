package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.dto.MonitorDataEx;
import com.carroll.biz.monitor.analyzer.model.MonitorItem;
//import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.request.*;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.MonitorItemListResponse;
import com.carroll.biz.monitor.common.response.MonitorItemPageResponse;
import com.carroll.biz.monitor.common.response.MonitorItemResponse;
import com.carroll.spring.rest.starter.BaseException;
//import com.carroll.spring.rest.starter.BaseException;

/**
 * 监控项管理
 * Created on 202017/11/22 14:25 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface IMonitorItemService {

    /**
     * 根据监控项tag 获取监控项
     *
     * @param tag
     * @return
     */
    MonitorItem getByTag(String tag, String projectId);

    MonitorItem getById(String id);

    MonitorItem save(MonitorItem item);

    MonitorItemListResponse list(BmBaseRequest request);

    MonitorItemResponse detail(IdRequest request) throws BaseException;

    MonitorItemPageResponse page(MonitorItemPageRequest request);

    MonitorItemResponse update(MonitorItemUpdateRequest request) throws BaseException;

    MonitorBaseResponse notify(MonitorItemNotifyRequest request) throws BaseException;

    MonitorItemResponse save(MonitorItemRequest request) throws BaseException;


    /**
     * 初始化系统tag
     */
    void initSystemMonitorItem();

    /**
     * 判断是否监控数据表示的服务状态
     *
     * @param monitorItem
     * @return
     */
    boolean isSuccess(MonitorItem monitorItem, MonitorDataEx data);

    /**
     * 修改状态
     *
     * @param id
     * @param status
     */
    void changeStatus(String id, MonitorItem.Status status) throws BaseException;

    /**
     * 删除监控项
     *
     * @param id
     */
    void delete(String id) throws BaseException;
}
