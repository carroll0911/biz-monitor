package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.MonitorTarget;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
public class MonitorTargetRepositoryImpl extends MongodbBaseDao<MonitorTarget> {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Page<MonitorTarget> advanceQuery(List<String> projectIds, String keyword, Pageable pageable) {
        Criteria criteria = new Criteria();
        if (!CollectionUtils.isEmpty(projectIds)) {
            if (projectIds.size() == 1) {
                criteria.and("projectId").is(projectIds.get(0));
            } else {
                criteria.and("projectId").in(projectIds);
            }
        }

        if (!StringUtils.isEmpty(keyword)) {
            Criteria criteria1;
            criteria1 = new Criteria();
            Pattern pattern = Pattern.compile("^.*" + keyword + ".*$", Pattern.CASE_INSENSITIVE);
            criteria1 = criteria1.orOperator(Criteria.where("name").regex(pattern), Criteria.where("code").regex(pattern));
            criteria.andOperator(criteria1);
        }
        Query query = new Query(criteria);
        query.with(new Sort(Sort.Direction.DESC, "updateTime"));
        return getPage(query, pageable);
    }

    @Override
    protected Class<MonitorTarget> getEntityClass() {
        return MonitorTarget.class;
    }

    @Override
    protected MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }
}
