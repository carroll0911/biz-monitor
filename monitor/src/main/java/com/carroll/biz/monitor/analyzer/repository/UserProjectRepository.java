package com.carroll.biz.monitor.analyzer.repository;

import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.model.UserProject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created on 202017/11/22 13:41 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface UserProjectRepository extends MongoRepository<UserProject, String> {

    Long deleteAllByProjectId(String projectId);

    List<UserProject> findAllByUserId(String userId);

    List<UserProject> findAllByProjectId(String projectId);

    List<UserProject> findAllByProjectIdAndRole(String projectId, Role role);

    List<UserProject> findAllByProjectIdIsIn(List<String> projectId);

}
