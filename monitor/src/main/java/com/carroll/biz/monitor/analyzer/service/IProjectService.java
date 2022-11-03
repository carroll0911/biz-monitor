package com.carroll.biz.monitor.analyzer.service;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.response.ProjectDetailResponse;
import com.carroll.biz.monitor.analyzer.dto.ProjectListDto;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.common.request.ProjectRequest;
import com.carroll.biz.monitor.common.request.ProjectUpdateRequest;
import com.carroll.biz.monitor.common.response.ProjectResponse;
import com.carroll.spring.rest.starter.BaseException;

import java.util.List;

/**
 * 产品项目管理
 * Created on 202017/11/22 13:45 By hehongbo
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
public interface IProjectService {

    /**
     * 根据Tag获取项目信息
     * @param tag
     * @return
     */
    Project getByTag(String tag);

    /**
     * 根据id获取项目信息
     * @param id
     * @return
     */
    Project getByID(String id);

    /**
     * 将project数据写入redis
     */
    void refreshProject();

    /**
     * 新增project
     * @param request
     * @return
     */
    ProjectResponse save(ProjectRequest request) throws BaseException;

    /**
     * 修改project
     * @param request
     * @return
     */
    ProjectResponse update(ProjectUpdateRequest request) throws BaseException;

    /**
     * 获取所有项目
     * @return
     */
    List<Project> findAll();

    /**
     * 项目列表
     * @return
     */
    List<ProjectListDto> list();

    /**
     * 获取项目详情
     * @param projectId
     * @return
     */
    ProjectDetailResponse detail(String projectId) throws MonitorBaseException;

    /**
     * 重置密码
     */
    void resetPassword(String projectId) throws BaseException;

    /**
     * 修改状态
     * @param projectId
     * @param status
     */
    void changeStatus(String projectId, Project.Status status) throws BaseException;
}
