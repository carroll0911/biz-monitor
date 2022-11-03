package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created on 202017/11/22 13:41 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface ProjectRepository extends MongoRepository<Project, String> {

    Project findByTag(String tag);

    Project findByIdIsNotAndTag(String id, String tag);

    List<Project> findByIdIsIn(List<String> ids);
}
