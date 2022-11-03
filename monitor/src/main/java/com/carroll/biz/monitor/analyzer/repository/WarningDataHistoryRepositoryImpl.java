package com.carroll.biz.monitor.analyzer.repository;

import com.mongodb.BasicDBObject;
import com.carroll.biz.monitor.analyzer.model.WarningDataHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * Created on 2017/11/30 15:11 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public class WarningDataHistoryRepositoryImpl extends MongodbBaseDao<WarningDataHistory> {

    private static final String STATUS_KEY = "status";
    private static final String PROJECT_ID_KEY = "projectId";
    private static final String ITEM_ID_KEY = "itemId";
    private static final String FIRST_TIME_KEY = "firstTime";
    private static final String APP_NAME_KEY = "applicationName";
    private static final String TARGET_KEY = "target";

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<WarningDataHistory> advanceQuery(Date firstStartTime, Date firstEndTime, Date recoveryStartTime, Date recoveryEndTime,
                                                 List<String> itemIds, String applicationName, String target, List<String> projectIds, Pageable pageable) {
        BasicDBObject query = new BasicDBObject();
        Criteria criteria = new Criteria();
        if (firstStartTime != null || firstEndTime != null) {
            BasicDBObject firstTime = null;
            criteria = criteria.and(FIRST_TIME_KEY);
            if (firstStartTime != null) {
                criteria = criteria.gte(firstStartTime);
                firstTime = (new BasicDBObject("$gte", firstStartTime));
            }
            if (firstEndTime != null) {
                criteria = criteria.lte(firstEndTime);
                if (firstTime == null) {
                    firstTime = (new BasicDBObject("$lte", firstEndTime));
                } else {
                    firstTime.append("$lte", firstEndTime);
                }
            }
            query.put(FIRST_TIME_KEY, firstTime);
        }

        if (recoveryStartTime != null || recoveryEndTime != null) {
            criteria = criteria.and("recoveryTime");
            BasicDBObject recoveryTime = null;
            if (recoveryStartTime != null) {
                criteria = criteria.gte(recoveryStartTime);
                recoveryTime = (new BasicDBObject("$gte", recoveryStartTime));
            }
            if (recoveryEndTime != null) {
                criteria = criteria.lte(recoveryEndTime);
                if (recoveryTime == null) {
                    recoveryTime = (new BasicDBObject("$lte", recoveryEndTime));
                } else {
                    recoveryTime.append("$lte", recoveryEndTime);
                }
            }
            query.put("recoveryTime", recoveryTime);
        }
        if (!CollectionUtils.isEmpty(itemIds)) {
            if (itemIds.size() == 1) {
                query.put(ITEM_ID_KEY, itemIds.get(0));
                criteria = criteria.and(ITEM_ID_KEY).is(itemIds.get(0));
            } else {
                query.put(ITEM_ID_KEY, new BasicDBObject("$in", itemIds));
                criteria = criteria.and(ITEM_ID_KEY).in(itemIds);
            }
        }
        if (!CollectionUtils.isEmpty(projectIds)) {
            if (projectIds.size() == 1) {
                query.put(PROJECT_ID_KEY, projectIds.get(0));
                criteria = criteria.and(PROJECT_ID_KEY).is(projectIds.get(0));
            } else {
                query.put(PROJECT_ID_KEY, new BasicDBObject("$in", projectIds));
                criteria = criteria.and(PROJECT_ID_KEY).in(projectIds);
            }
        }
        if (!StringUtils.isEmpty(applicationName)) {
            query.put(APP_NAME_KEY, applicationName);
            criteria = criteria.and(APP_NAME_KEY).is(applicationName);
        }
        if (!StringUtils.isEmpty(target)) {
            query.put(TARGET_KEY, target);
            criteria = criteria.and(TARGET_KEY).is(target);
        }
        Query query2 = new Query(criteria);
        query2.with(new Sort(Sort.Direction.DESC, "recoveryTime"));
        return getPage(query2, query, pageable);
    }

    @Override
    protected Class<WarningDataHistory> getEntityClass() {
        return WarningDataHistory.class;
    }

    @Override
    protected MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    private Page<WarningDataHistory> getPage(Query query, BasicDBObject queryObject, Pageable pageable) {
        String collectionName = this.getMongoTemplate().getCollectionName(WarningDataHistory.class);
        long totalCount = this.getMongoTemplate().getCollection(collectionName).countDocuments(queryObject);
//        long totalCount = this.getMongoTemplate().count(query,);
        query.skip(pageable.getOffset());// skip相当于从那条记录开始
        query.limit(pageable.getPageSize());// 从skip开始,取多少条记录
        List<WarningDataHistory> datas = this.find(query);
        return new PageImpl<>(datas, pageable, totalCount);
    }
}
