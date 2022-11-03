package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.WarningData;
import org.springframework.stereotype.Repository;

/**
 * Created on 2017/11/24 15:58 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Repository
public class WarningDataMongoDao extends MongoGenDao<WarningData> {
    @Override
    protected Class<WarningData> getEntityClass() {
        return WarningData.class;
    }
}
