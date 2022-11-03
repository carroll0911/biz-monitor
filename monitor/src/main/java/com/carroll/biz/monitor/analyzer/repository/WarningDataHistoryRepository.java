package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.WarningDataHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @author: hehongbo
 * @date 2019/10/23
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public interface WarningDataHistoryRepository extends MongoRepository<WarningDataHistory, String> {

    Page<WarningDataHistory> advanceQuery(Date firstStartTime, Date firstEndTime, Date recoveryStartTime, Date recoveryEndTime,
                                   List<String> itemIds, String applicationName, String target, List<String> projectIds, Pageable pageable);

    int countAllByItemId(String itemId);
}
