package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.BaseServiceMonitorItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created on 202017/11/22 13:41 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface BaseServiceMonitorItemRepository extends MongoRepository<BaseServiceMonitorItem, String> {

    List<BaseServiceMonitorItem> findAllByTagAndProjectId(String tag,String projectId);
}
