package com.carroll.biz.monitor.analyzer.controller;

import com.carroll.biz.monitor.analyzer.exception.MonitorBaseException;
import com.carroll.biz.monitor.analyzer.request.MonitorTargetPageRequest;
import com.carroll.biz.monitor.analyzer.request.MonitorTargetSaveRequest;
import com.carroll.biz.monitor.analyzer.response.MonitorTargetDetailResponse;
import com.carroll.biz.monitor.analyzer.response.MonitorTargetPageResponse;
import com.carroll.biz.monitor.analyzer.service.IMonitorTargetService;
import com.carroll.biz.monitor.common.response.MonitorBaseResponse;
import com.carroll.spring.rest.starter.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 告警对象管理接口
 *
 * @author: hehongbo
 * @date 2020/4/14
 * Copyright @2020 Tima Networks Inc. All Rights Reserved. 
 */
@Slf4j
@Api(value = "MonitorTargetController", description = "告警对象管理接口")
@RestController
@RequestMapping("/monitor-target")
public class MonitorTargetController extends BaseController {
    @Autowired
    private IMonitorTargetService monitorTargetService;

    @ApiOperation(value = "保存告警对象")
    @RequestMapping(method = RequestMethod.POST)
    public MonitorBaseResponse save(@Valid @RequestBody MonitorTargetSaveRequest request) throws MonitorBaseException {
        monitorTargetService.save(request);
        return new MonitorBaseResponse();
    }

    @ApiOperation(value = "分页查询")
    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public MonitorTargetPageResponse page(MonitorTargetPageRequest request) {
        return monitorTargetService.page(request);
    }

    @ApiOperation(value = "详情")
    @RequestMapping(method = RequestMethod.GET)
    public MonitorTargetDetailResponse detail(@RequestParam("id") String id) throws MonitorBaseException {
        return monitorTargetService.detail(id);
    }

    @ApiOperation(value = "删除")
    @RequestMapping(method = RequestMethod.DELETE)
    public MonitorBaseResponse delete(@RequestParam("id") String id) throws MonitorBaseException {
        monitorTargetService.delete(id);
        return new MonitorBaseResponse();
    }
}
