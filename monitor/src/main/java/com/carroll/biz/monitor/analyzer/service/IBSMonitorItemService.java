package com.carroll.biz.monitor.analyzer.service;/**
 * Created by core_ on 2018/5/9.
 */

import com.carroll.biz.monitor.analyzer.model.BaseServiceMonitorItem;

import java.util.List;

/**
 * Created on 202018/5/9 15:50 By hehongbo
 * <p>
 * Copyright @ 2018 Tima Networks Inc. All Rights Reserved. 
 */
public interface IBSMonitorItemService {

    List<BaseServiceMonitorItem> findAllByTagAndProjectId(String tag, String projectId);
}
