package com.carroll.biz.monitor.analyzer.controller.v2;

import com.carroll.spring.rest.starter.BaseController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.model.Project;
import com.carroll.biz.monitor.analyzer.request.IdRequest;
import com.carroll.biz.monitor.analyzer.response.ProjectDetailResponse;
import com.carroll.biz.monitor.analyzer.response.ProjectListResponse;
import com.carroll.biz.monitor.analyzer.service.IProjectService;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.ProjectRequest;
import com.carroll.biz.monitor.common.request.ProjectUpdateRequest;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.ProjectResponse;
import com.carroll.spring.rest.starter.BaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created on 2017/12/8 16:31 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "ProjectController", description = "运营项目管理")
@RestController
@RequestMapping("/v2/project")
public class ProjectControllerV2 extends BaseController {

    @Autowired
    private IProjectService projectService;

    @ApiOperation(value = "手动将project数据刷入共享数据,仅供内部使用")
    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "refreshFallBack", ignoreExceptions = {BaseException.class})
    public MonitorBaseResponse refresh() {
        projectService.refreshProject();
        return new MonitorBaseResponse();
    }

    @ApiOperation(value = "新增项目")
    @RequestMapping(method = RequestMethod.POST)
    public ProjectResponse save(@Valid @RequestBody ProjectRequest request, BindingResult result) throws BaseException {
        return projectService.save(request);
    }

    @ApiOperation(value = "修改项目")
    @RequestMapping(method = RequestMethod.PUT)
    public ProjectResponse update(@Valid @RequestBody ProjectUpdateRequest request, BindingResult result) throws BaseException {
        return projectService.update(request);
    }

    @ApiOperation(value = "查询所有项目")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ProjectListResponse list() {
        ProjectListResponse response = new ProjectListResponse();
        response.setList(projectService.list());
        return response;
    }

    @ApiOperation(value = "查询告警内容详情")
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ProjectDetailResponse detail(@RequestParam(name = "id") String id) throws MonitorBaseException {
        return projectService.detail(id);
    }

    @ApiOperation(value = "重置密码")
    @RequestMapping(value = "reset-pwd", method = RequestMethod.PUT)
    public MonitorBaseResponse resetPwd(@Valid @RequestBody IdRequest request, BindingResult result) throws BaseException {
        projectService.resetPassword(request.getId());
        return new MonitorBaseResponse();
    }

    @ApiOperation(value = "禁用")
    @RequestMapping(value = "disable", method = RequestMethod.PUT)
    public MonitorBaseResponse disable(@Valid @RequestBody IdRequest request, BindingResult result) throws BaseException {
        projectService.changeStatus(request.getId(), Project.Status.DISABLED);
        return new MonitorBaseResponse();
    }

    @ApiOperation(value = "启用")
    @RequestMapping(value = "enable", method = RequestMethod.PUT)
    public MonitorBaseResponse enable(@Valid @RequestBody IdRequest request, BindingResult result) throws BaseException {
        projectService.changeStatus(request.getId(), Project.Status.ENABLED);
        return new MonitorBaseResponse();
    }

    @SuppressWarnings("unused")
    public MonitorBaseResponse refreshFallBack(Throwable throwable) {
        return fallBackResponse(throwable);
    }

    private MonitorBaseResponse fallBackResponse(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
        MonitorBaseResponse response = new MonitorBaseResponse();
        response.setReturnSuccess(false);
        response.setReturnErrMsg(ErrEnum.SERVICE_UNAVAILABLE.getMsg());
        response.setReturnErrCode(ErrEnum.SERVICE_UNAVAILABLE.getCode());
        return response;
    }
}
