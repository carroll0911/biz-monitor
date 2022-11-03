package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.NotifyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created on 202017/11/22 13:41 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface NotifyRecordRepository extends MongoRepository<NotifyRecord, String> {

}
