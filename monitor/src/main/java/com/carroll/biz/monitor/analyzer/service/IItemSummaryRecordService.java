package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.response.ItemSummary4LineResponse;
import com.carroll.biz.monitor.analyzer.model.ItemSummaryRecord;

import java.util.Date;

/**
 * @author: hehongbo
 * @date 2019/10/16
 * Copyright @2019 Tima Networks Inc. All Rights Reserved. 
 */
public interface IItemSummaryRecordService {
    /**
     * 查询监控项统计记录
     * @param itemId
     * @param date
     * @return
     */
    ItemSummaryRecord find(String itemId, Date date);

    /**
     * Redis中查询监控项统计记录
     * @param itemId
     * @param date
     * @return
     */
    ItemSummaryRecord findFromRedis(String itemId, Date date);

    /**
     * 保存监控项统计记录
     * @param record
     * @return
     */
    ItemSummaryRecord save(ItemSummaryRecord record);

    /**
     * 监控统计曲线图数据
     * @return
     */
    ItemSummary4LineResponse getSummary4Line(String projectId, String type);
}
