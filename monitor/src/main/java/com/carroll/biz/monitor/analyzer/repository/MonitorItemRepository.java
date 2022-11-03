package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.MonitorItem;
import com.carroll.biz.monitor.analyzer.model.Operator;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created on 202017/11/22 13:41 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface MonitorItemRepository extends MongoRepository<MonitorItem, String> {

    MonitorItem findByTag(String tag);

    MonitorItem findTopByTagAndProjectId(String tag, String projectId);

    @Cacheable(value = "monitorItem#2*60*60")
    MonitorItem findByProjectIdAndId(String projectId, String id);

    @Cacheable(value = "monitorItem#list#byProject#2*60*60")
    List<MonitorItem> findByProjectId(String projectId);

    List<MonitorItem> findByProjectIdIsIn(List<String> projectIds);

    @Cacheable(value = "monitorItem#list#byLevel#2*60*60")
    List<MonitorItem> findByLevelAndProjectId(MonitorItem.Level level, String projectId);

    List<MonitorItem> advanceQuery(List<String> projectIds, String id, String level);

    Page<MonitorItem> advanceQuery(String projectId, Pageable pageable);

    Page<MonitorItem> advanceQuery(String projectId, String keyword, Pageable pageable);
    Page<MonitorItem> advanceQuery(List<String> projectIds, String keyword, Pageable pageable);

    @Cacheable(value = "monitorItem#list#ByReceiver#2*60*60")
    List<MonitorItem> findByReceivers(Operator operator);
}
