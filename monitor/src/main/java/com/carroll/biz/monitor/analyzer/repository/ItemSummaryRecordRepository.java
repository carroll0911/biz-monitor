package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.ItemSummaryRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * @author: hehongbo
 * @date 2019/10/16
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public interface ItemSummaryRecordRepository extends MongoRepository<ItemSummaryRecord, String> {

    /**
     * 根据监控项和日期查询
     *
     * @param itemId
     * @param date
     * @return
     */
    ItemSummaryRecord findTopByItemIdAndDate(String itemId, Date date);

    List<ItemSummaryRecord> findAllByItemIdInAndDateBetween(List<String> itemIds, Date start, Date end);
}
