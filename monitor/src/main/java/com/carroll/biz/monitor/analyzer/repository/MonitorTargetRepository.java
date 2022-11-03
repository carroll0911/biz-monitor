package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.MonitorTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
public interface MonitorTargetRepository extends MongoRepository<MonitorTarget, String> {

    MonitorTarget findTopByCodeAndProjectId(String code,String projectId);

    Page<MonitorTarget> advanceQuery(List<String> projectIds, String keyword, Pageable pageable);
}
