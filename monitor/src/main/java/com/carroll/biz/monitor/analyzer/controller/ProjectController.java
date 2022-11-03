package com.carroll.biz.monitor.analyzer.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.carroll.biz.monitor.analyzer.dto.ProjectListDto;
import com.carroll.biz.monitor.analyzer.dto.UserCacheDto;
import com.carroll.biz.monitor.analyzer.enums.Role;
import com.carroll.biz.monitor.analyzer.response.ProjectListResponse;
import com.carroll.biz.monitor.analyzer.service.IProjectService;
import com.carroll.biz.monitor.analyzer.utils.BizContext;
import com.carroll.biz.monitor.common.enums.ErrEnum;
import com.carroll.biz.monitor.common.request.ProjectRequest;
import com.carroll.biz.monitor.common.request.ProjectUpdateRequest;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.biz.monitor.common.response.ProjectResponse;
import com.carroll.spring.rest.starter.BaseController;
import com.carroll.spring.rest.starter.BaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2017/12/8 16:31 By lanli
 * <p>
 * Copyright @ 2017 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "ProjectController", description = "运营项目管理")
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {

    @Autowired
    private IProjectService projectService;

    @ApiOperation(value = "手动将project数据刷入共享数据,仅供内部使用")
    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "refreshFallBack", ignoreExceptions = {BaseException.class})
    public MonitorBaseResponse refresh() {
        projectService.refreshProject();
        return new MonitorBaseResponse();
    }

    @ApiOperation(value = "新增项目,仅供内部使用")
    @RequestMapping(method = RequestMethod.POST)
    public ProjectResponse save(@Valid @RequestBody ProjectRequest request, BindingResult result) throws BaseException {
        return projectService.save(request);
    }

    @ApiOperation(value = "修改项目,仅供内部使用")
    @RequestMapping(method = RequestMethod.PUT)
    public ProjectResponse update(@Valid @RequestBody ProjectUpdateRequest request, BindingResult result) throws BaseException {
        return projectService.update(request);
    }

    @ApiOperation(value = "查询所有项目")
    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ProjectListResponse list(@RequestParam(value = "status", required = false) String status) {
        ProjectListResponse response = new ProjectListResponse();
        response.setList(new ArrayList<>());
        UserCacheDto userCacheDto = (UserCacheDto) BizContext.getData(BizContext.MONITOR_USER_CACHE);
        if (userCacheDto == null || (CollectionUtils.isEmpty(userCacheDto.getProjects()) && !Role.SUPPER.equals(userCacheDto.getRole()))) {
            return response;
        }
        List<ProjectListDto> projects = projectService.list();
        if (Role.SUPPER.equals(userCacheDto.getRole())) {
            response.getList().addAll(projects);
        } else {
            for (ProjectListDto project : projects) {
                for (String pid : userCacheDto.getProjects().keySet()) {
                    if (project.getId().equals(pid)) {
                        response.getList().add(project);
                    }
                }
            }
        }
        if(!StringUtils.isEmpty(status)){
            response.setList(response.getList().stream().filter(p->p.getStatus().name().equals(status)).collect(Collectors.toList()));
        }
        return response;
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
