package com.carroll.biz.monitor.analyzer.service.impl;

import com.carroll.biz.monitor.analyzer.repository.BaseServiceMonitorItemRepository;
import com.carroll.biz.monitor.analyzer.model.BaseServiceMonitorItem;
import com.carroll.biz.monitor.analyzer.service.IBSMonitorItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created on 202018/5/9 15:51 By hehongbo
 * <p>
 * Copyright @ 2018 Tima Networks Inc. All Rights Reserved. 
 */
@Service
public class BSMonitorItemServiceImpl implements IBSMonitorItemService {

    @Autowired
    private BaseServiceMonitorItemRepository baseServiceMonitorItemRepository;

    @Override
    public List<BaseServiceMonitorItem> findAllByTagAndProjectId(String tag, String projectId) {
        return baseServiceMonitorItemRepository.findAllByTagAndProjectId(tag,projectId);
    }
}
